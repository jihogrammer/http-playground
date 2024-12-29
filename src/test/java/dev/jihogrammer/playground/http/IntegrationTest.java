package dev.jihogrammer.playground.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected String convertPayloadToString(final Object payload) {
        try {
            return this.objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

}
