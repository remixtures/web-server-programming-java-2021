package euroshopper;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@Points("05-03.3")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class C_OrderServiceTest {

    private static final boolean WITH_SESSION = true;
    private static final boolean WITHOUT_SESSION = false;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void itemsThatHaveBeenAddedToCartAreOrdered() throws Exception {
        Item item = createItem();

        Long itemCount = new Long(2 + new Random().nextInt(3));
        for (int i = 0; i < itemCount; i++) {
            addToCart(item, WITH_SESSION);
        }

        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        performOrder(name, address, WITH_SESSION);

        assertFalse("When making a POST-request to /orders, the items in the shopping cart should be ordered.", findOrdersWithUserAndItem(name, address, item.getName(), itemCount).isEmpty());

        MvcResult res = mockMvc.perform(get("/cart").session(mockSession))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("items")).andReturn();

        Map<Item, Long> counts = (Map) res.getModelAndView().getModel().get("items");
        assertTrue("When the order has been done, the cart should be empty.", counts.isEmpty());
    }

    @Test
    public void itemsThatHaveBeenAddedToCartMustNotBeOrderedIfNotSameUser() throws Exception {
        Item item = createItem();

        Long itemCount = new Long(2 + new Random().nextInt(3));
        for (int i = 0; i < itemCount; i++) {
            addToCart(item, WITHOUT_SESSION);
        }

        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 6);

        performOrder(name, address, WITHOUT_SESSION);

        assertTrue("When making a POST-request to /orders, the items in a shopping cart must not be ordered if the user is different.", findOrdersWithUserAndItem(name, address, item.getName(), itemCount).isEmpty());
    }

    private void addToCart(Item item, boolean withSession) throws Exception {
        MockHttpServletRequestBuilder request = post("/cart/items/{id}", item.getId());

        if (withSession) {
            request.session(mockSession);
        }

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    private void performOrder(String username, String address, boolean withSession) throws Exception {
        MockHttpServletRequestBuilder request = post("/orders").param("name", username).param("address", address);

        if (withSession) {
            request.session(mockSession);
        }

        mockMvc.perform(request).andExpect(status().is3xxRedirection());
    }

    private Item createItem() {
        Item item = new Item();
        String itemName = "Item: " + UUID.randomUUID().toString().substring(0, 6);
        item.setName(itemName);
        item.setPrice(10 * Math.random());
        return itemRepository.save(item);
    }

    private List<Order> findOrdersWithUserAndItem(String name, String address, String itemName, Long itemCount) {
        return orderRepository
                .findAll()
                .stream()
                .filter(o -> o.getName().equals(name) && o.getAddress().equals(address) && o.getOrderItems() != null && o.getOrderItems()
                .stream().filter(i -> i.getItem() != null && i.getItem().getId() != null && itemName.equals(i.getItem().getName()) && itemCount.equals(i.getItemCount())).count() > 0)
                .collect(Collectors.toList());
    }

}
