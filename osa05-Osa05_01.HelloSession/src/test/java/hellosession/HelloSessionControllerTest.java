package hellosession;

import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("05-01")
@ActiveProfiles("test")
public class HelloSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloSession() throws Throwable {
        MvcResult res = mockMvc.perform(get("/"))
                .andExpect(content().string("Hello there!"))
                .andReturn();

        mockMvc.perform(get("/").session((MockHttpSession) res.getRequest().getSession()))
                .andExpect(content().string("Hello again!"));

        mockMvc.perform(get("/").session((MockHttpSession) res.getRequest().getSession()))
                .andExpect(content().string("Hello again!"));

        mockMvc.perform(get("/"))
                .andExpect(content().string("Hello there!"))
                .andReturn();
    }

}
