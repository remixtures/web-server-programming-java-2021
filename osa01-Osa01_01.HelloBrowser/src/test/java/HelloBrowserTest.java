
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.fail;
import org.junit.Rule;
import org.junit.Test;

@Points("01-01")
public class HelloBrowserTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void noTests() {
        io.setSysIn("completelyrandomaddressthatdoesnotexisthopefullyplz");
        try {
            HelloBrowser.main(new String[]{});
        } catch (Throwable t) {
            return;
        }

        fail("If the address is not available, the application should throw an error.");
    }

}
