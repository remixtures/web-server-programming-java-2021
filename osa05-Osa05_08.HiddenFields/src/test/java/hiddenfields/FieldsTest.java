package hiddenfields;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("05-08")
@ActiveProfiles("test")
public class FieldsTest extends FluentTest {

    @LocalServerPort
    private Integer port;

    private String MESSAGES_URI;
    private String LOGOUT_URI;

    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        MESSAGES_URI = "http://localhost:" + port + "/messages";
        LOGOUT_URI = "http://localhost:" + port + "/logout";

        Message msg = new Message();
        msg.setContent("Jeff Davis");
        messageRepository.save(msg);
    }

    @Test
    public void pageShouldNotBeDirectlyAccessible() {
        goTo(MESSAGES_URI);
        assertFalse(hasText("Jeff Davis"));
    }

    @Test
    public void shouldSeeLoginPageOnAccessingMessages() {
        goTo(MESSAGES_URI);
        assertFalse($(By.name("username")).isEmpty());
        assertFalse($(By.name("password")).isEmpty());
    }

    @Test
    public void noAuthOnWrongPassword() {
        goTo(MESSAGES_URI);
        enterDetailsAndSubmit("Onni", "v123");
        assertFalse(hasText("Jeff Davis"));
    }

    @Test
    public void authSuccessfulAsUser() {
        goTo(MESSAGES_URI);
        enterDetailsAndSubmit("user", "password");
        assertTrue(hasText("Jeff Davis"));
    }

    @Test
    public void logoutSuccessful() {
        goTo(MESSAGES_URI);
        enterDetailsAndSubmit("user", "password");
        assertTrue(hasText("Jeff Davis"));
        $(By.name("logout")).click();
        goTo(MESSAGES_URI);
        assertFalse(hasText("Jeff Davis"));
    }

    @Test
    public void userDoesNotSeeForm() {
        goTo(MESSAGES_URI);
        enterDetailsAndSubmit("user", "password");
        assertFalse(hasText("Pow!"));
        boolean found = false;
        try {
            $(By.name("content")).click();
            found = true;
        } catch (NoSuchElementException t) {

        }

        assertFalse(found);
    }

    @Test
    public void postmanSeesForm() {
        goTo(MESSAGES_URI);
        enterDetailsAndSubmit("postman", "pat");
        assertTrue(hasText("Pow!"));
    }

    @Test
    public void postmanCanSubmitForm() {
        goTo(MESSAGES_URI);
        enterDetailsAndSubmit("postman", "pat");
        assertTrue(hasText("Pow!"));

        String msg = UUID.randomUUID().toString().substring(0, 8);
        $(By.name("content")).fill().with(msg);
        $(By.name("content")).submit();

        assertTrue(pageSource(), hasText(msg));
    }

//    @Test
//    public void userCantHackForm() {
//        goTo(MESSAGES_URI);
//        enterDetailsAndSubmit("user", "password");
//        String message = "trololo";
//
//        // might be buggy :(
//        createFormAndPost(message);
//
//        assertFalse(hasText(message));
//    }
    private void enterDetailsAndSubmit(String username, String password) {
        $(By.name("username")).fill().with(username);
        $(By.name("password")).fill().with(password);
        $(By.name("password")).submit();
    }

    private boolean hasText(String text) {
        return pageSource().toLowerCase().contains(text.toLowerCase());
    }

}
