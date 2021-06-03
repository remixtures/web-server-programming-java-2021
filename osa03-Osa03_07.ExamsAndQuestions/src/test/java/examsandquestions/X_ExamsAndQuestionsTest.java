package examsandquestions;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("03-07.1 03-07.2")
public class X_ExamsAndQuestionsTest extends FluentTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void canAddQuestion() throws Throwable {
        addQuestion();
    }

    @Test
    public void canAddExam() throws Throwable {
        addExam();
    }

    @Test
    public void addQuestionToExamAndShowExam() throws Throwable {
        Question q = addQuestion();
        Exam e = addExam();

        goTo("http://localhost:" + port + "/exams");
        $(By.partialLinkText(e.getSubject())).click();

        assertThat(pageSource()).contains(e.getSubject());
        assertThat(pageSource()).contains(q.getTitle());
        assertThat(pageSource()).doesNotContain(q.getContent());

        FluentWebElement el = $(By.tagName("li")).stream().filter(f -> f.textContent().contains(q.getTitle())).findFirst().get();

        el.find(By.tagName("form")).submit();

        assertThat(pageSource()).contains(e.getSubject());
        assertThat(pageSource()).contains(q.getTitle());
        assertThat(pageSource()).contains(q.getContent());

    }

    public Question addQuestion() {

        goTo("http://localhost:" + port + "/questions");

        Question question = new Question();
        question.setTitle("Title: " + UUID.randomUUID().toString().substring(0, 6));
        question.setContent("Content: " + UUID.randomUUID().toString().substring(0, 6));

        $("input[name=title]").fill().with(question.getTitle());
        $("textarea[name=content]").fill().with(question.getContent());
        $("input[value='Add']").click();

        assertThat(pageSource()).contains(question.getTitle());
        assertThat(pageSource()).contains(question.getContent());

        return question;
    }

    public Exam addExam() {

        goTo("http://localhost:" + port + "/exams");

        Exam exam = new Exam();
        exam.setSubject("Subject: " + UUID.randomUUID().toString().substring(0, 6));
        exam.setExamDate(DateTestUtils.getRandomDateBetween(2015, 2021));
        
        // date is entered to form using pattern "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String examDateParam = formatter.format(exam.getExamDate());
        
        $("input[name=subject]").fill().with(exam.getSubject());
        $("input[name=examDate]").fill().with(examDateParam);
        $("input[value='Add']").click();
        assertThat(pageSource()).contains(exam.getSubject());

        return exam;
    }

}
