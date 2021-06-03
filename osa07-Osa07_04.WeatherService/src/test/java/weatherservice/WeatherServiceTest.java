package weatherservice;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("07-04")
public class WeatherServiceTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLocationsRetrievalCached() throws Exception {
        cacheManager.getCache("locations").clear();

        MvcResult res = mockMvc.perform(get("/locations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("locations"))
                .andReturn();

        int size = ((List) res.getModelAndView().getModel().get("locations")).size();

        locationRepository.deleteAll();
        Location location = new Location();
        location.setName("kumpula");
        locationRepository.save(location);

        res = mockMvc.perform(get("/locations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("locations"))
                .andReturn();

        assertTrue(((List) res.getModelAndView().getModel().get("locations")).size() == size);

        location = new Location();
        location.setName("oomp-pula");
        locationRepository.save(location);

        res = mockMvc.perform(get("/locations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("locations"))
                .andReturn();

        assertTrue(((List) res.getModelAndView().getModel().get("locations")).size() == size);
    }

    @Test
    public void testSingleLocationCached() throws Exception {
        cacheManager.getCache("locations").clear();

        locationRepository.deleteAll();
        Location location = new Location();
        location.setName("kumpula");
        location = locationRepository.save(location);

        MvcResult res = mockMvc.perform(get("/locations/{id}", location.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("location"))
                .andReturn();

        assertTrue(((Location) res.getModelAndView().getModel().get("location")).getName().equals("kumpula"));

        location.setName("porkkana");
        location = locationRepository.save(location);

        res = mockMvc.perform(get("/locations/{id}", location.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("location"))
                .andReturn();

        assertTrue(((Location) res.getModelAndView().getModel().get("location")).getName().equals("kumpula"));
    }

    @Test
    public void testLocationsCacheEvictedOnAddLocation() throws Exception {
        cacheManager.getCache("locations").clear();

        locationRepository.deleteAll();
        Location location = new Location();
        location.setName("kumpula");
        locationRepository.save(location);

        MvcResult res = mockMvc.perform(get("/locations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("locations"))
                .andReturn();

        assertTrue(((List) res.getModelAndView().getModel().get("locations")).size() == 1);

        location = new Location();
        location.setName("oomp-pula");
        locationRepository.save(location);

        mockMvc.perform(post("/locations").param("name", "Bamboo Forest, Japan"))
                .andExpect(status().is3xxRedirection());

        res = mockMvc.perform(get("/locations"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("locations"))
                .andReturn();

        assertTrue(((List) res.getModelAndView().getModel().get("locations")).size() == 3);
    }

    @Test
    public void testLocationCacheEvictedOnAddLocation() throws Exception {
        cacheManager.getCache("locations").clear();

        locationRepository.deleteAll();
        Location location = new Location();
        location.setName("kumpula");
        location = locationRepository.save(location);

        MvcResult res = mockMvc.perform(get("/locations/{id}", location.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("location"))
                .andReturn();

        assertTrue(((Location) res.getModelAndView().getModel().get("location")).getName().equals("kumpula"));

        location.setName("porkkana");
        location = locationRepository.save(location);

        mockMvc.perform(post("/locations").param("name", "Bamboo Forest, Japan"))
                .andExpect(status().is3xxRedirection());

        res = mockMvc.perform(get("/locations/{id}", location.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("location"))
                .andReturn();

        assertTrue(((Location) res.getModelAndView().getModel().get("location")).getName().equals("porkkana"));
    }

    @Test
    public void testLocationCacheEvictedOnCacheFlush() throws Exception {
        cacheManager.getCache("locations").clear();

        locationRepository.deleteAll();
        Location location = new Location();
        location.setName("kumpula");
        location = locationRepository.save(location);

        MvcResult res = mockMvc.perform(get("/locations/{id}", location.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("location"))
                .andReturn();

        assertTrue(((Location) res.getModelAndView().getModel().get("location")).getName().equals("kumpula"));

        location.setName("porkkana");
        location = locationRepository.save(location);

        mockMvc.perform(get("/flushcaches"));

        res = mockMvc.perform(get("/locations/{id}", location.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("location"))
                .andReturn();

        assertTrue(((Location) res.getModelAndView().getModel().get("location")).getName().equals("porkkana"));
    }
}
