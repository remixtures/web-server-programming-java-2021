package hellothymeleaf;

import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("02-01")
public class HelloThymeleafTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void indexShownAtRoot() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello Thymeleaf")))
                .andExpect(content().string(not(containsString("dQw4w9WgXcQ"))));
    }

    @Test
    public void videoShownAtVideo() throws Exception {
        this.mockMvc.perform(get("/video")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("dQw4w9WgXcQ")))
                .andExpect(content().string(not(containsString("Hello Thymeleaf"))));
    }
}
