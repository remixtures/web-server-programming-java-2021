/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airports;

import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author miguel
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AircraftControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AircraftRepository aircraftRepository;
    
    @After
    public void tearDown() {
        aircraftRepository.deleteAll();
    }
    
    @Test
    public void testGetAircrafts() throws Exception {
        mockMvc.perform(get("/aircrafts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("aircrafts"))
                .andExpect(model().attributeExists("airports"));
    }
    
    @Test
    public void testPostAircraft() throws Exception {
        String aircraftName = "HA-LOL";
        
        assertEquals(0, aircraftRepository.count());
        mockMvc.perform(post("/aircrafts").param("name", aircraftName))
                .andExpect(redirectedUrl("/aircrafts"))
                .andExpect(status().is3xxRedirection());
        
        List<Aircraft> aircrafts = aircraftRepository.findAll();
        assertEquals(1, aircrafts.size());
        assertEquals(aircraftName, aircrafts.get(0).getName());
    }
    
    @Test
    public void testPostAndGetAircraft() throws Exception {
        String aircraftName ="XP-55";
        
        mockMvc.perform(post("/aircrafts").param("name", aircraftName))
                .andExpect(redirectedUrl("/aircrafts"))
                .andExpect(status().is3xxRedirection());
        MvcResult result = mockMvc.perform(get("/aircrafts")).andReturn();
        List<Aircraft> aircrafts = (List) result.getModelAndView().getModel().get("/aircrafts");
        assertEquals(1, aircrafts.size());
        assertEquals(aircraftName, aircrafts.get(0).getName());
    }
}