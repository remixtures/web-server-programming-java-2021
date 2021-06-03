package examsandquestions;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@Points("03-07.1 03-07.2")
public class ExamControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void canCreateAndListExams() throws Throwable {
        Map<Exam, Boolean> exams = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            exams.put(createExam(), false);
        }

        MvcResult res = mockMvc.perform(get("/exams"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("exams"))
                .andExpect(view().name("exams"))
                .andReturn();

        List<Exam> returnedExams = new ArrayList<>();
        try {
            returnedExams.addAll((Collection<Exam>) res.getModelAndView().getModel().get("exams"));
        } catch (Throwable t) {
            try {
                returnedExams.addAll(((Page<Exam>) res.getModelAndView().getModel().get("exams")).getContent());
            } catch (Throwable t2) {
                fail("Unable to retrieve exams from model.");
            }
        }
        
        for (Exam returnedExam : returnedExams) {
            for (Exam exam : exams.keySet()) {
                if(exam.getSubject().equals(returnedExam.getSubject()) && exam.getExamDate().equals(returnedExam.getExamDate())) {
                    exams.put(exam, true);
                }
            }
        }
        
        boolean allExamsFound = exams.values().stream().filter(f -> f == false).count() <= 0L;
        
        assertTrue("Verify that once new questions have been posted, they are also added to the response. Verify also their parameters etc.", allExamsFound);
    }

    @Test
    public void canViewSingleExam() throws Throwable {
        for (int i = 0; i < 3; i++) {
            createExam();
        }

        MvcResult res = mockMvc.perform(get("/exams"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("exams"))
                .andExpect(view().name("exams"))
                .andReturn();

        List<Exam> exams = new ArrayList<>();
        try {
            exams.addAll((Collection<Exam>) res.getModelAndView().getModel().get("exams"));
        } catch (Throwable t) {
            try {
                exams.addAll(((Page<Exam>) res.getModelAndView().getModel().get("exams")).getContent());
            } catch (Throwable t2) {
                fail("Unable to retrieve exams from model.");
            }
        }

        for (Exam exam : exams) {

            res = mockMvc.perform(get("/exams/" + exam.getId()))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(model().attributeExists("exam"))
                    .andExpect(view().name("exam"))
                    .andReturn();

            Exam e = (Exam) res.getModelAndView().getModel().get("exam");

            assertEquals(exam.getSubject(), e.getSubject());
            assertEquals(exam.getExamDate(), e.getExamDate());
        }
    }

    private Exam createExam() throws Throwable {
        Exam e = new Exam();
        e.setSubject(UUID.randomUUID().toString().substring(0, 6));

        LocalDate randomExamDate = DateTestUtils.getRandomLocalDateBetween(2015, 2021);
        e.setExamDate(randomExamDate);

        String examDateParam = randomExamDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        mockMvc.perform(
                post("/exams").param("subject", e.getSubject()).param("examDate", examDateParam)
        ).andExpect(status().is3xxRedirection());

        return e;
    }

}
