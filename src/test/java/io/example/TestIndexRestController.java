package io.example;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by gmind on 2015-10-06.
 */
public class TestIndexRestController extends TestBootConfig {

    @Test
    public void index() throws Exception {
        this.document.snippets(
                links(
                        linkWithRel("doctors").description("The <<resources-doctors,Doctors resource>>"),
                        linkWithRel("patient").description("The <<resources-patients,Patients resource>>"),
                        linkWithRel("schedule").description("The <<resources-schedules,Schedules resource>>")),
                responseFields(
                        fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources")));

        this.mockMvc.perform(get("/").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
    }

}
