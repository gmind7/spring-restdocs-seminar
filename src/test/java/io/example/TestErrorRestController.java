package io.example;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gmind on 2015-10-06.
 */
public class TestErrorRestController extends TestBootConfig {

    @Test
    public void clientErrorBadRequest() throws Exception {
        this.document.snippets(responseFields(
                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                fieldWithPath("message").description("A description of the cause of the error"),
                fieldWithPath("path").description("The path to which the request was made"),
                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred")));

        this.mockMvc
                .perform(get("/error").accept(MediaTypes.HAL_JSON)
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/doctors/123")
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "The tag 'http://localhost:8080/doctors/123' does not exist"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is(notNullValue())));
    }

}
