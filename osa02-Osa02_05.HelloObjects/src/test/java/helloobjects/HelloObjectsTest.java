package helloobjects;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
@Points("02-05")
public class HelloObjectsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addItem() throws Exception {
        addItems(1);
    }

    @Test
    public void addMultipleItems() throws Exception {
        List<Item> items = addItems(5);
        
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            
            String pageSource = pageSource();
            
            assertThat(pageSource).containsOnlyOnce(item.getName());
            assertThat(pageSource).containsOnlyOnce(item.getType());           
        }
    }

    private List<Item> addItems(int count) throws Exception {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            String name = "NAME: " + UUID.randomUUID().toString();
            String type = "TYPE: " + UUID.randomUUID().toString();

            this.mockMvc.perform(post("/").param("name", name).param("type", type))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

            items.add(new Item(name, type));
            
            String pageSource = pageSource();

            assertThat(pageSource).containsOnlyOnce(name);
            assertThat(pageSource).containsOnlyOnce(type);
        }

        return items;
    }

    public String pageSource() throws Exception {

        return this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
