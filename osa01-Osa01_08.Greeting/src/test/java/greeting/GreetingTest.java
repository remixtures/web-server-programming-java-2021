package greeting;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("01-08")
public class GreetingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test1() throws Exception {
        this.mockMvc.perform(get("/greet?greeting=Greetings&name=Earthling"))
                .andExpect(content().string("Greetings, Earthling"));
    }

    @Test
    public void paramTest2() throws Exception {
        this.mockMvc.perform(get("/greet?greeting=Oi&name=Mate"))
                .andExpect(content().string("Oi, Mate"));
    }

    @Test
    public void randomPathTest() throws Exception {
        this.mockMvc.perform(get("/" + UUID.randomUUID().toString()))
                .andExpect(status().is(404));
    }

}
