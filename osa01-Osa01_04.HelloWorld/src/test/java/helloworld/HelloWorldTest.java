package helloworld;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("01-04")
public class HelloWorldTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWorldTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(content().string("Hello World!"));

    }

    @Test
    public void helloWorldAnyUrlTest() throws Exception {
        this.mockMvc.perform(get("/" + UUID.randomUUID().toString()))
                .andExpect(content().string("Hello World!"));

    }
}
