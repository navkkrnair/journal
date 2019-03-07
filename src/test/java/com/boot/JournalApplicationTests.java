package com.boot;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.boot.entity.Journal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JournalApplicationTests
{

    private final String SPRING_BOOT_MATCH = "Spring Boot";
    private final String CLOUD_MATCH       = "Cloud";
    private final String KUBIA_MATCH       = "Kubernetes";
    private MediaType    contentType       = new MediaType(MediaType.APPLICATION_JSON
            .getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    // Loads a WebApplicationContext and provides a mock servlet environment.
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    @Before
    public void setup() throws Exception
    {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAll() throws Exception
    {
        mockMvc.perform(get("/journal/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$",
                                    iterableWithSize(5)))
                .andExpect(jsonPath("$[0]['title']",
                                    containsString(SPRING_BOOT_MATCH)));
    }

    @Test
    public void findByCloudTitle() throws Exception
    {
        mockMvc.perform(get("/journal/findBy/title/" + CLOUD_MATCH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$",
                                    iterableWithSize(1)))
                .andExpect(jsonPath("$[0]['title']",
                                    containsString(CLOUD_MATCH)));
    }
    
    @Test
    public void findByKubiaTitle() throws Exception
    {
        mockMvc.perform(get("/journal/findBy/title/" + KUBIA_MATCH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$",
                                    iterableWithSize(1)))
                .andExpect(jsonPath("$[0]['title']",
                                    containsString(KUBIA_MATCH)));
    }

    @Test
    public void add() throws Exception
    {
        mockMvc.perform(post("/journal").content(this
                .toJsonString(new Journal("Spring Boot Testing", "Create Spring Boot Tests", "18-01-2019")))
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    protected String toJsonString(Object obj) throws IOException
    {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.converter.write(obj,
                             MediaType.APPLICATION_JSON,
                             mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
