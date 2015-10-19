package io.example;

import lombok.Getter;
import lombok.Setter;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * Created by gmind on 2015-10-06.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleBootRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@WebAppConfiguration
public class TestBootConfig {

@Rule
public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

@Autowired
private WebApplicationContext context;

public RestDocumentationResultHandler document;

public MockMvc mockMvc;

@Before
public void setUp() {
    this.document = document("{method-name}",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()));
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply(documentationConfiguration(this.restDocumentation)
                    .uris()
                        .withScheme("http")
                        .withHost("root-endpoint")
                    .and()
                    .snippets()
                        .withEncoding("UTF-8"))
            .alwaysDo(this.document)
            .build();
}

    @Test
    public void bulkSkipTest() {
    }


}
