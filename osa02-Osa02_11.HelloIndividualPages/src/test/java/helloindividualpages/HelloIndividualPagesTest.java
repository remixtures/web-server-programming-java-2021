package helloindividualpages;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.junit.FluentTest;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Points("02-11")
public class HelloIndividualPagesTest extends FluentTest {

    @LocalServerPort
    int port;

    private List<Item> items = new ArrayList<>();

    @Test
    public void addItem() {
        items.addAll(addItems(1));
    }

    @Test
    public void addMultipleItems() {
        addItems(5).stream().forEach(i -> {
            assertThat(pageSource()).containsOnlyOnce(i.getName());
            assertThat(pageSource()).containsOnlyOnce(i.getType());
            items.add(i);
        });
    }

    @Test
    public void addItemAndVisitPage() {
        addItems(1).stream().forEach(i -> {
            goTo("http://localhost:" + port + "/");
            $("a", withText(i.getName())).click();

            assertThat(pageSource()).contains(i.getName());
            assertThat(pageSource()).contains(i.getType());

            items.stream().forEach(s -> assertThat(pageSource()).doesNotContain(s.getName()));
        });
    }

    @Test
    public void addMultipleItemsAndVisitEachPage() {
        addItems(1).stream().forEach(i -> {
            goTo("http://localhost:" + port + "/");
            $("a", withText(i.getName())).click();

            assertThat(pageSource()).contains(i.getName());
            assertThat(pageSource()).contains(i.getType());

            items.stream().forEach(s -> assertThat(pageSource()).doesNotContain(s.getName()));
        });
    }

    private List<Item> addItems(int count) {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            goTo("http://localhost:" + port + "/");

            String name = "NAME: " + UUID.randomUUID().toString();
            String type = "TYPE: " + UUID.randomUUID().toString();

            items.add(new Item(name, type));

            $("input[name=name]").fill().with(name);
            $("input[name=type]").fill().with(type);
            $("input[type=submit]").submit();

            getDriver().navigate().refresh();

            assertThat(pageSource()).containsOnlyOnce(name);
            assertThat(pageSource()).containsOnlyOnce(type);
        }

        return items;
    }
}
