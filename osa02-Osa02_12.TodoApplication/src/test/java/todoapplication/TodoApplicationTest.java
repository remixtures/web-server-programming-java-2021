package todoapplication;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("02-12.1 02-12.2")
@ActiveProfiles("test")
public class TodoApplicationTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    int port;

    @Test
    public void canAddItem() {
        addItem();
    }

    @Test
    public void canAddMultipleItems() {
        addItems(5);
    }

    @Test
    public void canAccessIndividualPages() {
        List<String> items = addItems(5);

        items.stream().forEach(item -> {
            goTo("http://localhost:" + port + "/");
            find("a", withText(item)).click();

            items.stream().filter(s -> !s.equals(item)).forEach(s -> assertThat(pageSource()).doesNotContain(s));
        });
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

        return itemName;
    }
}
