package onlyfortheselected;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("05-07")
@ActiveProfiles("test")
public class OnlyForTheSelectedApplicationTest extends FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void init() {
        if (accountRepository.findByUsername("larry") == null) {
            Account larry = new Account();
            larry.setUsername("larry");
            larry.setPassword(passwordEncoder.encode("larry"));
            larry.setAuthorities(Arrays.asList("USER"));
            accountRepository.save(larry);
        }
        
        if (accountRepository.findByUsername("moe") == null) {
            Account moe = new Account();
            moe.setUsername("moe");
            moe.setPassword(passwordEncoder.encode("moe"));
            moe.setAuthorities(Arrays.asList("USER", "ADMIN"));
            accountRepository.save(moe);
        }

        if (accountRepository.findByUsername("curly") == null) {
            Account curly = new Account();
            curly.setUsername("curly");
            curly.setPassword(passwordEncoder.encode("curly"));
            curly.setAuthorities(Arrays.asList("ADMIN"));
            accountRepository.save(curly);
        }

        getDriver().manage().deleteAllCookies();
    }

    @Test
    public void anyoneCanSeeHappypath() throws Throwable {
        goTo("http://localhost:" + port + "/happypath");
        assertThat(pageSource()).contains("Happy!");
    }

    @Test
    public void userCanSeeSecretAfterLogin() throws Throwable {
        goTo("http://localhost:" + port + "/secretpath");
        assertThat(pageSource()).doesNotContain("Secret!");
        enterDetailsAndSubmit("larry", "larry");
        assertThat(pageSource()).contains("Secret!");
    }

    @Test
    public void userCannotSeeAdminPathEvenAfterLogin() throws Throwable {
        goTo("http://localhost:" + port + "/adminpath");
        assertThat(pageSource()).doesNotContain("Admin!");
        enterDetailsAndSubmit("larry", "larry");
        assertThat(pageSource()).doesNotContain("Admin!");
    }

    @Test
    public void adminCanSeeAdminPathAfterLogin() throws Throwable {
        goTo("http://localhost:" + port + "/adminpath");
        assertThat(pageSource()).doesNotContain("Admin!");
        enterDetailsAndSubmit("curly", "curly");
        assertThat(pageSource()).contains("Admin!");
    }

    private void enterDetailsAndSubmit(String username, String password) {
        $(By.name("username")).fill().with(username);
        $(By.name("password")).fill().with(password);
        $(By.name("password")).submit();
    }
}
