package uk.ac.ebi.subs.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.repository.model.templates.*;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TemplateSerialisationTest {

    private Template template;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void buildUp() {
        template = Template.builder()
                .name("test")
                .targetType("things")
                .build();

        template
                .add(
                        "thingy",
                        FieldCapture.builder().fieldName("thingy").build()
                )
                .add("whatsit",
                        AttributeCapture.builder().build()
                )
                .add(
                        "whatdoyoumacallit",
                        NoOpCapture.builder().build()
                );

        template.setDefaultCapture(
                AttributeCapture.builder().build()
        );
    }

    @Test
    public void serialisationCycle() throws IOException {
        String jsonSerialisation = objectMapper.writeValueAsString(template);

        Template deserialisedCopy = objectMapper.readValue(jsonSerialisation, Template.class);

        Assert.assertEquals(template, deserialisedCopy);
    }


}
