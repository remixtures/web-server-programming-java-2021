package reloadheroes;

import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("05-02")
@ActiveProfiles("test")
public class ReloadHeroesTest extends org.fluentlenium.adapter.junit.FluentTest {

    @org.springframework.boot.web.server.LocalServerPort
    private Integer port;

    @Autowired
    private ReloadStatusRepository reloadStatusRepository;

    @Test
    public void reloadsIncrementOnPageload() throws Throwable {
        getDriver().manage().deleteAllCookies();

        goTo("http://localhost:" + port);
        String username = $(By.id("name")).textContents().get(0);
        int reloads = Integer.parseInt($(By.id("reloads")).textContents().get(0));

        assertTrue(reloads == 1);

        goTo("http://localhost:" + port);
        assertThat($(By.id("name")).textContents().get(0)).contains(username);
        assertThat($(By.id("reloads")).textContents().get(0)).contains("" + (reloads + 1));

        assertNotNull(reloadStatusRepository.findByName(username));
        assertTrue(2 == reloadStatusRepository.findByName(username).getReloads());
    }

    @Test
    public void pageShowsContentFromDatabase() throws Throwable {
        getDriver().manage().deleteAllCookies();

        goTo("http://localhost:" + port);
        String username = $(By.id("name")).textContents().get(0);
        int reloads = Integer.parseInt($(By.id("reloads")).textContents().get(0));

        assertTrue(reloads == 1);

        ReloadStatus reloadStatus = reloadStatusRepository.findByName(username);
        reloadStatus.setReloads(41);
        reloadStatusRepository.save(reloadStatus);

        goTo("http://localhost:" + port);
        assertNotNull(reloadStatusRepository.findByName(username));
        assertThat($(By.id("reloads")).textContents().get(0)).contains("42");
    }

}
