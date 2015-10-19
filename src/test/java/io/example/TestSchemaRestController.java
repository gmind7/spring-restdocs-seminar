package io.example;

import org.junit.Test;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by gmind on 2015-10-08.
 */
public class TestSchemaRestController extends TestBootConfig {

    @Test
    public void schema() throws Exception {
        this.document.snippets(responseHeaders(
                headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`")));
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
