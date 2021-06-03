package lastmessages;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@Points("03-06")
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LastMessagesTest {

    @Autowired
    JdbcTemplate template;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void onlyLatestContentIsShown() throws Exception {
        template.update("INSERT INTO Message (id, message, message_date) VALUES (1, 'leka', '2019-03-01 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (2, 'toka', '2019-03-02 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (3, 'kolmas', '2019-03-03 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (4, 'neljas', '2019-03-04 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (5, 'viides', '2019-03-05 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (6, 'kuudes', '2019-03-06 18:21:33.874')");

        MvcResult res = mockMvc.perform(get("/messages"))
                .andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse(content.contains("eka"));
        assertTrue(content.contains("toka"));
        assertTrue(content.contains("kuudes"));
    }

    @Test
    public void onlyLatestContentIsShown2() throws Exception {
        String random1 = UUID.randomUUID().toString();
        String random2 = UUID.randomUUID().toString();
        template.update("INSERT INTO Message (id, message, message_date) VALUES (13, '" + random1 + "', '2019-03-07 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (12, '" + random2 + "', '2019-03-08 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (11, 'kolmas', '2019-03-09 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (10, 'neljas', '2019-03-10 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (9, 'viides', '2019-03-11 18:21:33.874')");
        template.update("INSERT INTO Message (id, message, message_date) VALUES (8, 'kuudes', '2019-03-12 18:21:33.874')");

        MvcResult res = mockMvc.perform(get("/messages"))
                .andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse(content.contains(random1));
        assertTrue(content.contains(random2));
    }

}
