package registration;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import static org.junit.Assert.*;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Points("06-06")
@ActiveProfiles("test")
public class RegistrationAppTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void formReturnedOnGetToRegistrations() throws Exception {
        MvcResult res = mockMvc.perform(get("/registrations"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("Verify that a GET-request to /registrations returns a view created from the form.html-page.",
                "form", res.getModelAndView().getViewName());
    }

    @Test
    public void redirectToSuccessOnSuccessfulPostToRegister() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 10);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 4) + "@" + address.substring(0, 4) + ".fi";

        MvcResult res = mockMvc.perform(post("/registrations")
                .param("name", name)
                .param("address", address)
                .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/success")).andReturn();

//        assertEquals("Verify that a POST-request to /registrations (with valid data) returns a view created from the success.html-page.",
//                "success", res.getModelAndView().getViewName());
        boolean found = false;
        for (Registration registration : registrationRepository.findAll()) {
            if (registration.getName() == null) {
                continue;
            }

            if (registration.getName().equals(name)) {
                found = true;
                break;
            }
        }

        assertTrue("The registration must be added to database on success.", found);
    }

    @Test
    public void returnToFormOnTooShortName() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 3);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 1) + "@" + address.substring(0, 4) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the name is too short, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnTooLongName() throws Exception {
        String name = UUID.randomUUID().toString();
        while (name.length() < 50) {
            name += UUID.randomUUID().toString();
        }

        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 1) + "@" + address.substring(0, 4) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the name is too long, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnTooShortAddress() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 3);
        String email = name.substring(0, 4) + "@" + address.substring(0, 1) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the address is too short, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnTooLongAddress() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        address = address.substring(0, 51);
        String email = name.substring(0, 4) + "@" + address.substring(0, 1) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the address is too long, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnEmail() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 6);
        String address = UUID.randomUUID().toString().substring(0, 16);
        String email = name.substring(0, 3) + address.substring(0, 5) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When the email is invalid, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    @Test
    public void returnToFormOnAllFailure() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 2);
        String address = UUID.randomUUID().toString().substring(0, 2);
        String email = name.substring(0, 1) + address.substring(0, 1) + ".fi";

        verifyValidationsReturnToForm(name, address, email, "When any of the inputs is faulty, the user should be shown the form again with existing inputs and error messages.", "The registration must not be added to database on failure.");
    }

    private void verifyValidationsReturnToForm(String name, String address, String email, String viewFailsError, String inDatabaseError) {
        try {
            MvcResult res = mockMvc.perform(post("/registrations")
                    .param("name", name)
                    .param("address", address)
                    .param("email", email))
                    .andExpect(status().isOk()).andReturn();

            assertEquals("form", res.getModelAndView().getViewName());
        } catch (Throwable t) {
            fail(viewFailsError + " Error: " + t.getMessage());
        }

        boolean found = false;
        for (Registration registration : registrationRepository.findAll()) {
            if (registration.getName() == null) {
                continue;
            }

            if (registration.getName().equals(name)) {
                found = true;
                break;
            }
        }

        assertFalse(inDatabaseError, found);
    }
}
