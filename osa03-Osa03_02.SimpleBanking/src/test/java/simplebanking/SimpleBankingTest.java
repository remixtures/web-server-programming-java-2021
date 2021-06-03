package simplebanking;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.domain.FluentWebElement;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("03-02")
@ActiveProfiles("test")
public class SimpleBankingTest extends FluentTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @org.springframework.boot.web.server.LocalServerPort
    private Integer port;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void canAddClientWithAccount() {
        addClientAndAccount();
    }

    @Test
    public void canAddMultipleClientsWithAccounts() {
        List<String> clientAccountPairs = addClientsAndAccounts(5);

        goTo("http://localhost:" + port + "/");

        for (int i = 0; i < clientAccountPairs.size(); i++) {
            int hashIndex = clientAccountPairs.get(i).indexOf("#");
            String client = clientAccountPairs.get(i).substring(0, hashIndex);
            String account = clientAccountPairs.get(i).substring(hashIndex + 1);

            FluentWebElement table = $("table").first();
            List<FluentWebElement> rows = table.find("tr");

            boolean found = rows.stream().map(r -> r.text()).filter(t -> t.contains(client) && t.contains(account)).count() > 0;

            assertTrue(found);
        }
    }

    @Test
    public void clientWithSameNameStoredOnlyOnce() {
        List<String> clientAccountPairs = addClientsAndAccounts(5);

        long size = clientRepository.count();

        for (String clientAccountPair : clientAccountPairs) {
            int hashIndex = clientAccountPair.indexOf("#");
            String client = clientAccountPair.substring(0, hashIndex);

            addAccountToClient(client);
        }

        assertTrue(size == clientRepository.count());

    }

    public List<String> addClientsAndAccounts(int count) {
        return IntStream.range(0, count).mapToObj(i -> addClientAndAccount()).collect(Collectors.toList());
    }

    public String addClientAndAccount() {
        return addAccountToClient("Client: " + UUID.randomUUID().toString().substring(0, 6));
    }

    public String addAccountToClient(String client) {

        goTo("http://localhost:" + port + "/");

        String account = "Account: " + UUID.randomUUID().toString().substring(0, 6);

        $("input[name=name]").fill().with(client);
        $("input[name=iban]").fill().with(account);
        $("input[value='Add!']").click();

        assertThat(pageSource()).contains(client);
        assertThat(pageSource()).contains(account);

        return client + "#" + account;
    }
}
