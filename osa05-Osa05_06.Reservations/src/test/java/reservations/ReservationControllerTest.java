package reservations;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.time.LocalDate;
import java.util.Collection;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("05-06")
@ActiveProfiles("test")
public class ReservationControllerTest {

    @Autowired
    private ReservationTestUtilities testUtilities;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getReservationsReturnsReservationsAndView() throws Throwable {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("reservations"))
                .andExpect(view().name("reservations"))
                .andReturn();
    }

    @Test
    public void getReservationsReturnsListWithReservationsInDatabase() throws Throwable {
        testUtilities.clearReservations();

        MvcResult res = mockMvc.perform(get("/reservations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("reservations"))
                .andExpect(view().name("reservations"))
                .andReturn();

        Collection<Reservation> reservations = (Collection<Reservation>) res.getModelAndView().getModel().get("reservations");
        assertTrue(reservations.isEmpty());

        // add a reservation
        testUtilities.addReservation("user", LocalDate.of(2016, 1, 1), LocalDate.of(2016, 2, 1));

        res = mockMvc.perform(get("/reservations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("reservations"))
                .andExpect(view().name("reservations"))
                .andReturn();

        reservations = (Collection<Reservation>) res.getModelAndView().getModel().get("reservations");
        assertTrue(reservations.size() == 1);
        assertTrue(reservations.stream().filter(rs -> rs.getUser().getUsername().equals("user")).count() == 1);
    }
}
