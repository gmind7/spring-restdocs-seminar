package io.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import io.example.doctor.Doctor;
import io.example.doctor.DoctorJpaRepository;
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
public class TestDoctorsRestController extends TestBootConfig {

    @Autowired
    private DoctorJpaRepository doctorJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

@Test
public void doctorsShowAll() throws Exception {
    this.mockMvc.perform(get("/doctors").param("page","2").param("size","10"))
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
                fieldWithPath("_links").type(JsonFieldType.OBJECT).description("<<resources-doctors-show-all-links,Doctors>> Resources"),
                fieldWithPath("_embedded.doctors").type(JsonFieldType.OBJECT).description("<<resources-doctors-show-one, Doctor>> Resource").optional(),
                fieldWithPath("page").type(JsonFieldType.OBJECT).description("Information On <<overview-pagination, Pagination>>"))));
}

    @Test
    public void doctorsShowOne() throws Exception {
        Doctor doctor = this.doctorJpaRepository.findAll().get(0);
        this.mockMvc.perform(get("/doctors/{id}", doctor.getId()))
                .andExpect(status().isOk())
                .andDo(createDoctorsResultHandler(
                        linkWithRel("self").description("Self Rel Href"),
                        linkWithRel("doctor_schedules").description("<<resources-schedules-show-all-response-fields, Doctor Schedules>> Rel Href")));
    }

    @Test
    public void doctorsCreate() throws Exception {
        Map<String, String> create = Maps.newHashMap();
        create.put("name", "doctor_name_create");
        this.mockMvc.perform(
                put("/doctors")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(this.objectMapper.writeValueAsString(create)))
                .andExpect(header().string("Location", notNullValue()))
                .andDo(this.document.snippets(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("의사명")),
                        responseHeaders(headerWithName("Location").description("신규 생성된 자원 주소"))))
                .andReturn().getResponse().getHeader("Location");
    }

    @Test
    public void doctorsUpdate() throws Exception {
        Doctor doctor = this.doctorJpaRepository.findAll().get(0);
        Map<String, String> update = Maps.newHashMap();
        update.put("name", doctor.getName()+"_update");
        this.mockMvc.perform(
                patch("/doctors/{id}", doctor.getId())
                        .contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(update)))
                .andDo(this.document.snippets(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("의사명")),
                        pathParameters(parameterWithName("id").description("의사아이디"))))
                .andExpect(status().isNoContent());
    }

    @Test
    public void doctorsDelete() throws Exception {
        Doctor doctor = this.doctorJpaRepository.findAll().get(0);
        this.mockMvc.perform(
                delete("/doctors/{id}", doctor.getId())
                        .contentType(MediaTypes.HAL_JSON))
                .andDo(this.document.snippets(pathParameters(parameterWithName("id").description("의사아이디"))))
                .andExpect(status().isOk());
    }

    @Test
    public void doctorsSchdules() throws Exception {
        Doctor doctor = this.doctorJpaRepository.findAll().get(0);
        this.mockMvc.perform(get("/doctors/{id}/schedules", doctor.getId()))
                .andExpect(status().isOk());
    }

    public ResultHandler createDoctorsResultHandler(LinkDescriptor... linkDescriptors) {
        return this.document.snippets(
                links(linkDescriptors),
                pathParameters(
                        parameterWithName("id").description("의사아이디")),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("의사아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("의사명"),
                        fieldWithPath("_links").type(JsonFieldType.OBJECT).description("의사정보")));
    }

}
