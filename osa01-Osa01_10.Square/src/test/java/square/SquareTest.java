package square;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("01-10")
public class SquareTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void square() throws Exception {
        Random rand = new Random();

        for (int i = 0; i < 5; i++) {
            int num = rand.nextInt(1000000) - 500000;
            this.mockMvc.perform(get("/square?num=" + num))
                    .andExpect(content().string("" + (num * num)));
        }
    }
}
