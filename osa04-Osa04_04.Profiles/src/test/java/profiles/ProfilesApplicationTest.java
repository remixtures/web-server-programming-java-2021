package profiles;

import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Points("04-04")
public class ProfilesApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void correctProfileReturned() throws Throwable {
        this.mockMvc.perform(get("/profile"))
                .andExpect(content().string("test"));
    }

    @Test
    public void correctProfileShown() throws Throwable {
        MvcResult res = this.mockMvc.perform(get("/")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue("If the currently active profile is \"test\", then the text \"test\" should be visible on the HTML-page that is returned from GET /.", content.contains("test"));
        assertFalse("If the currently active profile is not \"somethingcompletelyrandom\", then the text \"somethingcompletelyrandom\" should not be visible on the HTML-page that is returned from GET /.", content.contains("somethingcompletelyrandom"));
    }

}
