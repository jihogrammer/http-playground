package dev.jihogrammer.playground.http.adaptor.web.relay;

import dev.jihogrammer.playground.http.IntegrationTest;
import dev.jihogrammer.playground.http.domain.HttpMethod;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class RelayControllerTest extends IntegrationTest {

    @Test
    void getGoogle() throws Exception {
        // given
        var payload = new RelayPayload(HttpMethod.GET, "https://www.google.com", null, null, null);
        var content = this.convertPayloadToString(payload);

        // when
        var requestBuilder = MockMvcRequestBuilders.post("/relay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // then
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}