package itemdatabase;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.junit.FluentTest;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("02-08")
@ActiveProfiles("test")
public class ItemDatabaseTest extends FluentTest {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private Integer port;

    @Test
    public void canAddItem() {
        addItem();
    }

    @Test
    public void canAddMultipleItems() {
        addItems(5);
    }

    public List<String> addItems(int count) {
        return IntStream.range(0, count).mapToObj(i -> addItem()).collect(Collectors.toList());
    }

    public String addItem() {

        goTo("http://localhost:" + port + "/");

        String itemName = "Item: " + UUID.randomUUID().toString();

        $("input[name=name]").fill().with(itemName);
        $("input[value='Add!']").click();

        assertThat(pageSource()).contains(itemName);

        assertEquals(1, jdbcTemplate.queryForList("SELECT * FROM Item").stream().filter(m -> m.values().stream().filter(v -> v.equals(itemName)).count() > 0).count());

        return itemName;
    }
}
