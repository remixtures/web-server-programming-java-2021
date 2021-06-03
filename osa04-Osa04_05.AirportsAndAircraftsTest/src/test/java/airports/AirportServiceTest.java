/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airports;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author miguel
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AirportServiceTest {
    
    @Autowired
    private AirportRepository airportRepository;
    
    @Autowired
    private AirportService airportService;
    
    @After
    public void tearDown() {
        airportRepository.deleteAll();
    }
    
    @Test
    public void testCreateNewAirport() {
        String identifier = "LIS";
        String name = "Lisbon";
        
        assertEquals(0, airportRepository.count());
        this.airportService.create(identifier, name);
        assertEquals(1, airportRepository.count());
        Airport portela = airportRepository.findAll().get(0);
        assertEquals(identifier, portela.getIdentifier());
        assertEquals(name, portela.getName());
    }
    
    @Test
    public void testListAllAirports() {
        Airport portela = new Airport();
        Airport saCarneiro = new Airport();
        Airport faro = new Airport();
       
        this.airportRepository.save(portela);
        this.airportRepository.save(saCarneiro);
        this.airportRepository.save(faro);
        
        List<Airport> airports = this.airportService.list();
        assertEquals(3, airports.size());
        assertEquals(portela, airportRepository.findAll().get(0));
        assertEquals(saCarneiro, airportRepository.findAll().get(1));
        assertEquals(faro, airportRepository.findAll().get(2));
    }
    
    @Test
    public void testCreateExistingAirport() {
        String identifier = "LIS";
        String name = "Lisbon";
        
        assertEquals(0, airportRepository.count());
        this.airportService.create(identifier, name);
        assertEquals(1, airportRepository.count());
        assertEquals(1, airportRepository.count());
        airportService.create(identifier, "Sá Carneiro");
        assertEquals(1, airportRepository.count());
        airportService.create("OPO", name);
        assertEquals(1, airportRepository.count());
    }
}