package gifbin;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Random;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Points("04-01")
public class GifBinTest {

    private final String API_URI = "/gifs";
    private final String API_URI_SINGLE = "/gifs/1";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void redirectedFromRoot() throws Exception {
        mockMvc.perform(get(API_URI))
                .andExpect(redirectedUrl(API_URI_SINGLE));
    }

    @Test
    public void responseContainsCount() throws Exception {
        mockMvc.perform(get(API_URI_SINGLE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("count"));
    }

    @Test
    public void canPostFile() throws Exception {
        // "aarrggghh" likely wont render -- we don't care :)
        MockMultipartFile multipartFile = new MockMultipartFile("file", "aarrggghh.gif", "image/gif", "aarrggghh".getBytes());

        mockMvc.perform(multipart(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));
    }

    @Test
    public void countIncreasesWhenPostingAFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "aarrggghh.gif", "image/gif", "aarrggghh".getBytes());

        Long count = getCount();
        mockMvc.perform(multipart(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        assertEquals("The count of images should increase when posting a valid file.", new Long(count + 1), getCount());
    }

    @Test
    public void countDoesNotIncreaseWhenPostingANonGif() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "aarrggghh.gif", "mmm/burgerz", "aarrggghh".getBytes());

        Long count = getCount();
        mockMvc.perform(multipart(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        assertEquals("When posting a file that is not a gif (\"image/gif\"), the count of images in database should not increase.", count, getCount());
    }

    private Long getCount() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI_SINGLE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("count")).andReturn();

        return Long.parseLong("" + res.getModelAndView().getModel().get("count"));
    }

}
