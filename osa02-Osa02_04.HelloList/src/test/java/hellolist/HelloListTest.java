package hellolist;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("02-04")
public class HelloListTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contentFromFormAddedToList() throws Exception {
        List<String> shouldNotContainItems = new ArrayList<>();
        Collections.addAll(shouldNotContainItems, "Abracadabra", "Alakazam", "Bibbidi-Bobbidi-Boo", "By the Power of Grayskull, I HAVE THE POWER", "Open sesame", "Shazam", "sudo get me a sandwich");

        List<String> shouldContainItems = new ArrayList<>();

        while (!shouldNotContainItems.isEmpty()) {

            String pageContent = this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            shouldNotContainItems.stream().forEach(s -> assertThat(pageContent).doesNotContain(s));
            shouldContainItems.stream().forEach(s -> assertThat(pageContent).contains(s));

            String itemToAdd = shouldNotContainItems.get(0);
            shouldNotContainItems.remove(0);
            shouldContainItems.add(itemToAdd);

            this.mockMvc.perform(post("/").param("content", itemToAdd))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

        }
    }
}
