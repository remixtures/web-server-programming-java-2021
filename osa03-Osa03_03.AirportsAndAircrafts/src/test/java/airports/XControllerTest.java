package airports;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Points("03-03.2")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class XControllerTest {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void canAddAircraftToAirport() throws Throwable {

        Aircraft ac = new Aircraft();
        ac.setName("HA-L0L");
        ac = aircraftRepository.save(ac);

        Airport ap = new Airport();
        ap.setName("Batman Airport");
        ap = airportRepository.save(ap);

        mockMvc.perform(post("/aircrafts/" + ac.getId() + "/airports")
                .param("airportId", "" + ap.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/aircrafts"));

        ac = aircraftRepository.getOne(ac.getId());

        Airport airport = ReflectionUtils.invokeMethod(Airport.class, ReflectionUtils.requireMethod(Aircraft.class, "getAirport"), ac);

        assertEquals("When a POST is made to /airports/{aircraftId}/airports, the posted airport should be set as the home airport of the aircraft with {aircraftId}.", ap.getId(), airport.getId());
    }

}
