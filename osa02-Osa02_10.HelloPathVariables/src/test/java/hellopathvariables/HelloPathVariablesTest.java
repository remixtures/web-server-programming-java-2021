package hellopathvariables;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("02-10")
public class HelloPathVariablesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void defaultGivesHat() throws Exception {
        assertThat(pageSource()).contains("Hat");
    }

    @Test
    public void allHatsAvailable() throws Exception {
        Map<String, Item> items = new HashMap<>();
        items.put("default", new Item("Hat", "default"));
        items.put("ascot", new Item("Ascot cap", "hat"));
        items.put("balaclava", new Item("Balaclava", "hat"));
        items.put("bicorne", new Item("Bicorne", "hat"));
        items.put("busby", new Item("Busby", "hat"));
        items.put("capotain", new Item("Capotain", "hat"));
        items.put("homburg", new Item("Homburg", "hat"));
        items.put("montera", new Item("Montera", "hat"));
        
        List<String> keys = new ArrayList<>();
        keys.addAll(items.keySet());
        
        for (int i = 0; i < keys.size(); i++) {
            String s = keys.get(i);
            String source = pageSource("/" + s);
            
            
            assertThat(source).contains(items.get(s).getName());

            items.keySet().stream().forEach(item -> {
                if (s.equals(item)) {
                    return;
                }

                assertThat(source).doesNotContain(items.get(item).getName());
            });
        }
    }

    public String pageSource() throws Exception {
        return pageSource("/");
    }

    public String pageSource(String path) throws Exception {
        return this.mockMvc.perform(get(path)).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
