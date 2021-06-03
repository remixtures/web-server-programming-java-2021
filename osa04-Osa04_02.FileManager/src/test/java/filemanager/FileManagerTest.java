package filemanager;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Points("04-02")
public class FileManagerTest {

    private final String API_URI = "/files";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getFilesReturnsSite() throws Exception {
        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("files"))
                .andReturn();

        assertTrue(res.getResponse().getContentAsString().contains("File manager"));
    }

    @Test
    public void canPostAFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "bac.on", "burgerz/mmm", "yay".getBytes());

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        Long largestFileId = largestFileId(res.getResponse().getContentAsString());
        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(largestFileId < largestFileId(res.getResponse().getContentAsString()));
    }

    @Test
    public void filenameVisibleAfterPost() throws Exception {
        String randomName = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, "burgerz/mmm", "yay".getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(res.getResponse().getContentAsString().contains(randomName));
    }

    @Test
    public void contentTypeVisibleAfterPost() throws Exception {
        String randomName = UUID.randomUUID().toString().substring(0, 6);
        String randomContentType = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, randomContentType, "yay".getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        assertTrue(res.getResponse().getContentAsString().contains(randomContentType));
    }

    @Test
    public void downloadedFileAvailableAndCorrect() throws Exception {
        String randomContent = UUID.randomUUID().toString().substring(0, 6);
        String randomName = UUID.randomUUID().toString().substring(0, 6);

        MockMultipartFile multipartFile = new MockMultipartFile("file", randomName, MediaType.IMAGE_GIF_VALUE, randomContent.getBytes());

        mockMvc.perform(fileUpload(API_URI).file(multipartFile))
                .andExpect(redirectedUrl(API_URI));

        MvcResult res = mockMvc.perform(get(API_URI))
                .andExpect(status().isOk()).andReturn();

        Long largestFileId = largestFileId(res.getResponse().getContentAsString());

        res = mockMvc.perform(get(API_URI + "/" + largestFileId))
                .andExpect(status().is2xxSuccessful()).andReturn();

        assertEquals(MediaType.IMAGE_GIF_VALUE, res.getResponse().getContentType());
        assertEquals(randomContent, new String(res.getResponse().getContentAsByteArray()));
    }

    private Long largestFileId(String content) {
        Pattern p = Pattern.compile("(files\\/[0-9]*)");
        Matcher matcher = p.matcher(content);

        Long largest = 0L;
        while (matcher.find()) {
            String res = matcher.group();
            res = res.substring(res.indexOf("/") + 1).trim();

            try {
                Long curr = Long.parseLong(res);
                if (curr > largest) {
                    largest = curr;
                }
            } catch (Throwable t) {
            }
        }

        return largest;
    }
}
