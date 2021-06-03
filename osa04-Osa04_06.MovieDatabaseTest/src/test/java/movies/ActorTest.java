/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movies;

import org.fluentlenium.adapter.junit.FluentTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author miguel
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActorTest extends FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Test
    public void testAddingAndRemovingActor() {
        goTo("http://localhost:" + port + "/actors");
        assertFalse(pageSource().contains("Uuno Turhapuro"));
        find("#name").fill().with("Uuno Turhapuro");
        find("form").first().submit();
        assertTrue(pageSource().contains("Uuno Turhapuro"));
        find("form").last().submit();
        goTo("http://localhost:" + port + "/movies");
        assertFalse(pageSource().contains("Uuno Turhapuro"));
    }
}