package euroshopper;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("05-03.2")
@ActiveProfiles("test")
public class B_CartControllerTest {

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shoppingCartLoadedAsBean() {
        assertTrue("Verify that you have annotated your ShoppingCart-class properly so that it is loaded to the context.", listableBeanFactory.containsBean("shoppingCart"));
    }

    @Test
    public void getCartAvailableAndModelItemsExists() throws Exception {
        MvcResult res = mockMvc.perform(get("/cart"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        assertNotNull("When a request is made to /cart, the content of the shopping cart should be added to the model.", res.getModelAndView().getModel().get("items"));

        Map<Item, Long> counts = null;
        try {
            counts = (Map) res.getModelAndView().getModel().get("items");
        } catch (Throwable t) {
            fail("The attribute \"items\" that is added to /cart-request should be of type Map<Item, Long>.");
        }
    }

    @Test
    public void getCartShowsCartJsp() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items"))
                .andExpect(view().name("cart"));
    }

    @Test
    public void addingItemToCartWorks() throws Exception {
        Item item = new Item();
        item.setName("Item: " + UUID.randomUUID().toString().substring(0, 6));
        item.setPrice(10 * Math.random());

        item = itemRepository.save(item);

        // add product to cart
        mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        // get cart
        MvcResult res = mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        Map<Item, Long> counts = (Map) res.getModelAndView().getModel().get("items");

        Long count = null;
        boolean found = false;
        for (Item i : counts.keySet()) {
            if (i.getName() != null && i.getName().equals(item.getName())) {
                found = true;
                count = counts.get(i);
            }
        }

        assertTrue("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should be in the model of a GET to /cart." + counts, found);
        assertTrue("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should be in the model of a GET to /cart.", count != null);

        assertEquals("When a item has been added to the cart once by making a POST-request to /cart/items/{id}, the \"items\" in the model of a GET to /cart should have 1 as the value of the item.", new Long(1), count);

        mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        res = mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        counts = (Map) res.getModelAndView().getModel().get("items");

        count = null;
        for (Item i : counts.keySet()) {
            if (i.getName() != null && i.getName().equals(item.getName())) {
                count = counts.get(i);
            }
        }

        assertTrue("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should be in the model of a GET to /cart.", count != null);

        assertEquals("When a item has been added to the cart three times by making a POST-request to /cart/items/{id}, the \"items\" in the model of a GET to /cart should have 3 as the value of the item.", new Long(3), count);

    }

    @Test
    public void addingWorks() throws Exception {
        Item item = new Item();
        item.setName("Item: " + UUID.randomUUID().toString().substring(0, 6));
        item.setPrice(10 * Math.random());

        item = itemRepository.save(item);

        // add product to cart
        mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        // get cart
        MvcResult res = mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        Map<Item, Long> counts = (Map) res.getModelAndView().getModel().get("items");

        Long count = null;
        boolean found = false;
        for (Item i : counts.keySet()) {
            if (i.getName() != null && i.getName().equals(item.getName())) {
                found = true;
                count = counts.get(i);
            }
        }

        assertTrue("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should be in the model of a GET to /cart." + counts, found);
        assertTrue("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should be in the model of a GET to /cart.", count != null);

        assertEquals("When a item has been added to the cart once by making a POST-request to /cart/items/{id}, the \"items\" in the model of a GET to /cart should have 1 as the value of the item.", new Long(1), count);

        mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        mockMvc.perform(post("/cart/items/{id}", item.getId()).session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        res = mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        counts = (Map) res.getModelAndView().getModel().get("items");

        count = null;
        for (Item i : counts.keySet()) {
            if (i.getName() != null && i.getName().equals(item.getName())) {
                count = counts.get(i);
            }
        }

        assertTrue("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should be in the model of a GET to /cart.", count != null);

        assertEquals("When a item has been added to the cart three times by making a POST-request to /cart/items/{id}, the \"items\" in the model of a GET to /cart should have 3 as the value of the item.", new Long(3), count);
    }

    @Test
    public void addingNotWorkingWhenNoSession() throws Exception {
        Item item = new Item();
        item.setName("Item: " + UUID.randomUUID().toString().substring(0, 6));
        item.setPrice(10 * Math.random());

        item = itemRepository.save(item);

        // add product to cart
        mockMvc.perform(post("/cart/items/{id}", item.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        // get cart
        MvcResult res = mockMvc.perform(get("/cart"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        Map<Item, Long> counts = (Map) res.getModelAndView().getModel().get("items");

        Long count = null;
        boolean found = false;
        for (Item i : counts.keySet()) {
            if (i.getName() != null && i.getName().equals(item.getName())) {
                found = true;
                count = counts.get(i);
            }
        }

        assertFalse("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should not be in the model of a GET to /cart if the user is not the same." + counts, found);
        assertFalse("When a item has been added to the cart by making a POST-request to /cart/items/{id}, the item should not be in the model of a GET to /cart if the user is not the same.", count != null);
    }
}
