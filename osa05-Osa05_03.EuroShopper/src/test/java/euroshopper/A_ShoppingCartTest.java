package euroshopper;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.ActiveProfiles;

@Points("05-03.1")
@ActiveProfiles("test")
public class A_ShoppingCartTest {

    Reflex.ClassRef<Object> klass;
    String klassName = "euroshopper.ShoppingCart";

    @Before
    public void setUp() {
        klass = Reflex.reflect("euroshopper.ShoppingCart");
    }

    @Test
    public void addToCartExists() {
        klass.method("addToCart").returningVoid().taking(Item.class).requirePublic();
    }

    @Test
    public void getItemsExists() {
        klass.method("getItems").returning(Map.class).takingNoParams().requirePublic();
    }

    @Test
    public void publicConstructorExists() {
        klass.ctor().takingNoParams().isPublic();
    }

    @Test
    public void addWorksForOneItem() throws Throwable {
        Object cart = klass.ctor().takingNoParams().invoke();
        Item i = new Item();
        i.setName("Porkkana");
        i.setPrice(0.5);

        klass.method("addToCart").returningVoid().taking(Item.class).invokeOn(cart, i);
        Map<Item, Long> content = (Map) klass.method("getItems").returning(Map.class).takingNoParams().invokeOn(cart);

        assertTrue("Item should be in the cart after it has been added to the cart.", content.containsKey(i));
        assertEquals("When item has been added to cart once, it's count should be one.", new Long(1), content.get(i));

        klass.method("addToCart").returningVoid().taking(Item.class).invokeOn(cart, i);
        klass.method("addToCart").returningVoid().taking(Item.class).invokeOn(cart, i);

        assertEquals("When item has been added to cart three times, it's count should be three.", new Long(3), content.get(i));
    }

    @Test
    public void addWorksForMultipleItems() throws Throwable {
        Object cart = klass.ctor().takingNoParams().invoke();
        Item porkkana = new Item();
        porkkana.setName("Porkkana");
        porkkana.setPrice(0.5);

        Item nauris = new Item();
        nauris.setName("Nauris");
        nauris.setPrice(0.25);

        klass.method("addToCart").returningVoid().taking(Item.class).invokeOn(cart, porkkana);
        Map<Item, Long> content = (Map) klass.method("getItems").returning(Map.class).takingNoParams().invokeOn(cart);

        assertTrue("Item should be in the cart after it has been added to the cart.", content.containsKey(porkkana));
        assertEquals("When item has been added to cart once, it's count should be one.", new Long(1), content.get(porkkana));

        klass.method("addToCart").returningVoid().taking(Item.class).invokeOn(cart, nauris);
        assertTrue("Item should be in the cart after it has been added to the cart.", content.containsKey(nauris));

        klass.method("addToCart").returningVoid().taking(Item.class).invokeOn(cart, porkkana);

        assertEquals("When item has been added to cart two times, it's count should be two.", new Long(2), content.get(porkkana));

        assertEquals("When item has been added to cart once, it's count should be one.", new Long(1), content.get(nauris));
    }
}
