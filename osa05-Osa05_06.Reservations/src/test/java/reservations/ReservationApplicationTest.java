package reservations;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.adapter.junit.FluentTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Points("05-06")
@ActiveProfiles("test")
public class ReservationApplicationTest extends FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ReservationTestUtilities testUtils;

    @Test
    public void canListReservations() throws Throwable {
        testUtils.clearReservations();

        goTo("http://localhost:" + port + "/reservations");

        assertThat(pageSource()).doesNotContain("bob");
        assertThat(pageSource()).doesNotContain("2016-01-01");
        assertThat(pageSource()).doesNotContain("2016-02-01");

        testUtils.addReservation("bob", LocalDate.of(2016, 1, 1), LocalDate.of(2016, 2, 1));
        goTo("http://localhost:" + port + "/reservations");

        assertThat(pageSource()).contains("bob");
        assertThat(pageSource()).contains("2016-01-01");
        assertThat(pageSource()).contains("2016-02-01");
    }

    @Test
    public void canNotAddReservationWithoutLoggingIn() throws Throwable {
        testUtils.clearReservations();

        goTo("http://localhost:" + port + "/reservations");

        $("input[name=reservationFrom]").fill().with("2016-03-01");
        $("input[name=reservationTo]").fill().with("2016-03-03");
        $("input[value='Add']").submit();

        assertThat(testUtils.reservationCount()).isEqualTo(0);
    }
    
    // TODO: need to fix setting data to HTML5 date fields

//    @Test
//    public void canAddReservationAfterLogin() throws Throwable {
//        testUtils.clearReservations();
//
//        goTo("http://localhost:" + port + "/login");
//
//        enterDetailsAndSubmit("user1", "user1");
//
//        goTo("http://localhost:" + port + "/reservations");
//
//        $("input[name=reservationFrom]").fill().with("2016-03-01");
//        $("input[name=reservationTo]").fill().with("2016-03-03");
//        $("input[value='Add']").submit();
//
//        goTo("http://localhost:" + port + "/reservations");
//
//        assertThat(pageSource()).contains("user1");
//
//        assertThat(testUtils.reservationCount()).isEqualTo(1);
//    }
//
//    @Test
//    public void cannotReserveOverlappingTimes() throws Throwable {
//        testUtils.clearReservations();
//
//        goTo("http://localhost:" + port + "/login");
//
//        enterDetailsAndSubmit("user1", "user1");
//
//        goTo("http://localhost:" + port + "/reservations");
//
//        $("input[name=reservationFrom]").fill().with("2016-03-02");
//        $("input[name=reservationTo]").fill().with("2016-03-05");
//        $("input[value='Add']").submit();
//
//        assertThat(testUtils.reservationCount()).isEqualTo(1);
//
//        // fully overlapping
//        $("input[name=reservationFrom]").fill().with("2016-03-01");
//        $("input[name=reservationTo]").fill().with("2016-03-06");
//        $("input[value='Add']").submit();
//
//        assertThat(testUtils.reservationCount()).isEqualTo(1);
//
//        // overlapping start
//        $("input[name=reservationFrom]").fill().with("2016-03-01");
//        $("input[name=reservationTo]").fill().with("2016-03-03");
//        $("input[value='Add']").submit();
//
//        assertThat(testUtils.reservationCount()).isEqualTo(1);
//
//        // overlapping end
//        $("input[name=reservationFrom]").fill().with("2016-03-04");
//        $("input[name=reservationTo]").fill().with("2016-03-07");
//        $("input[value='Add']").submit();
//
//        assertThat(testUtils.reservationCount()).isEqualTo(1);
//    }
//
//    @Test
//    public void canAddMultipleReservations() throws Throwable {
//        testUtils.clearReservations();
//
//        goTo("http://localhost:" + port + "/login");
//
//        enterDetailsAndSubmit("user1", "user1");
//
//        for (int i = 1; i < 10; i += 3) {
//
//            goTo("http://localhost:" + port + "/reservations");
//
//            $("input[name=reservationFrom]").fill().with("2016-03-0" + i);
//            $("input[name=reservationTo]").fill().with("2016-03-0" + (i + 2));
//            $("input[value='Add']").submit();
//
//        }
//
//        assertThat(testUtils.reservationCount()).isEqualTo(3);
//
//    }

    private void enterDetailsAndSubmit(String username, String password) {
        $(By.name("username")).fill().with(username);
        $(By.name("password")).fill().with(password);
        $(By.name("password")).submit();
    }
}
