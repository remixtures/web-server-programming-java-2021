package hellomodel;

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
@Points("02-03")
public class HelloModelTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void bothShownOnPage() throws Exception {
        this.mockMvc.perform(get("/?title=HelloWorld&person=Robot")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Robot")))
                .andExpect(content().string(containsString("HelloWorld")))
                .andExpect(content().string(not(containsString("Girl"))))
                .andExpect(content().string(not(containsString("SuperStory"))));
        this.mockMvc.perform(get("/?title=SuperStory&person=Girl")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Robot"))))
                .andExpect(content().string(not(containsString("HelloWorld"))))
                .andExpect(content().string(containsString("Girl")))
                .andExpect(content().string(containsString("SuperStory")));

    }
}
