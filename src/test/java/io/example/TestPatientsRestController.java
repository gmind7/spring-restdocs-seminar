package io.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.example.patient.Patient;
import io.example.patient.PatientJpaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gmind on 2015-10-06.
 */
public class TestPatientsRestController extends TestBootConfig {

    @Autowired
    private PatientJpaRepository patientJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void patientsShowAll() throws Exception {
        this.mockMvc.perform(get("/patients").param("page","2").param("size", "10"))
                .andExpect(status().isOk())
                .andDo(this.document.snippets(
                        links(
                                linkWithRel("next").optional().description("다음페이지"),
                                linkWithRel("prev").optional().description("이전페이지"),
                                linkWithRel("self").description("현재페이지")),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("리스트 사이즈")),
                        responseFields(
                                fieldWithPath("_links").type(JsonFieldType.OBJECT).description("<<resources-patients-show-all-links,Patients>> Resources"),
                                fieldWithPath("_embedded.patients").type(JsonFieldType.OBJECT).description("<<resources-patients-show-one, Patient>> Resource").optional(),
                                fieldWithPath("page").type(JsonFieldType.OBJECT).description("Information On <<overview-pagination, Pagination>>"))));
    }

    @Test
    public void patientsShowOne() throws Exception {
        Patient patient = this.patientJpaRepository.findAll().stream().filter(x -> x.getSchedules()!=null).findFirst().get();
        this.mockMvc.perform(get("/patients/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andDo(createPatientsResultHandler(
                        linkWithRel("self").description("Self Rel Href"),
                        linkWithRel("patient_schedules").optional().description("<<resources-schedules-show-all-response-fields, Patient Schedules>> Rel Href")));
    }

    @Test
    public void patientsCreate() throws Exception {
        Map<String, String> create = Maps.newHashMap();
        create.put("name", "patient_name_create");
        create.put("birthDate", "2015-10-01");
        this.mockMvc.perform(
                put("/patients")
                        .contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(create)))
                .andExpect(header().string("Location", notNullValue()))
                .andDo(this.document.snippets(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일(yyyy-MM-dd)")),
                        responseHeaders(
                            headerWithName("Location").description("신규 생성된 자원 주소"))))
                .andReturn().getResponse().getHeader("Location");
    }

    @Test
    public void patientsUpdate() throws Exception {
        Patient patient = this.patientJpaRepository.findAll().get(0);
        Map<String, String> update = Maps.newHashMap();
        update.put("name", patient.getName()+"_update");
        update.put("birthDate", "2015-10-02");
        this.mockMvc.perform(
                patch("/patients/{id}", patient.getId())
                        .contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(update)))
                .andDo(this.document.snippets(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일(yyyy-MM-dd)")),
                        pathParameters(parameterWithName("id").description("환자아이디"))))
                .andExpect(status().isNoContent());
    }

    @Test
    public void patientsDelete() throws Exception {
        Patient patient = this.patientJpaRepository.findAll().get(0);
        this.mockMvc.perform(
                delete("/patients/{id}", patient.getId())
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(this.document.snippets(pathParameters(parameterWithName("id").description("환자아이디"))))
                .andExpect(status().isOk());
    }

    @Test
    public void patientsSchdules() throws Exception {
        Patient patient = this.patientJpaRepository.findAll().get(0);
        this.mockMvc.perform(get("/patients/{id}/schedules", patient.getId()))
                .andExpect(status().isOk());
    }

    public ResultHandler createPatientsResultHandler(LinkDescriptor... linkDescriptors) {
        return this.document.snippets(
                links(linkDescriptors),
                pathParameters(
                        parameterWithName("id").description("환자아이디")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("환자아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("환자명"),
                        fieldWithPath("birthDate").type(JsonFieldType.OBJECT).description("환자생년월일"),
                        fieldWithPath("_links").type(JsonFieldType.OBJECT).description("환자정보")));
    }
}
