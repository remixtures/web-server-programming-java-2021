package helloauthentication;

import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("05-04")
@ActiveProfiles("test")
public class HelloAuthenticationTest extends org.fluentlenium.adapter.junit.FluentTest {

    @org.springframework.boot.web.server.LocalServerPort
    private Integer port;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Before
    public void init() {
        Message msg = new Message();
        msg.setContent("Jeff Davis");
        messageRepository.save(msg);
    }


    @Test
    public void pageShouldNotBeDirectlyAccessible() {
        goTo("http://localhost:" + port + "/messages");
        assertThat(pageSource()).doesNotContain("Jeff Davis");
    }

    @Test
    public void shouldSeeLoginPageOnAccessingMessages() {
        goTo("http://localhost:" + port + "/messages");
        assertThat(find(By.name("username"))).isNotNull();
        assertThat(find(By.name("password"))).isNotNull();
    }

    @Test
    public void noAuthOnWrongPassword() {
        goTo("http://localhost:" + port + "/messages");
        enterDetailsAndSubmit("Onni", "v123");
        assertThat(pageSource()).doesNotContain("Jeff Davis");
    }

    @Test
    public void authSuccessfulOnCorrectPassword() {
        goTo("http://localhost:" + port + "/messages");

        enterDetailsAndSubmit("maxwell_smart", "kenkapuhelin");
        assertThat(pageSource()).contains("Jeff Davis");
    }

    private void enterDetailsAndSubmit(String username, String password) {
        find(By.name("username")).fill().with(username);
        find(By.name("password")).fill().with(password);
        find(By.name("password")).submit();
    }
}
