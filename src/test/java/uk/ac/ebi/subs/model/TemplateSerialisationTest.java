package uk.ac.ebi.subs.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.ac.ebi.subs.repository.model.templates.AttributeCapture;
import uk.ac.ebi.subs.repository.model.templates.FieldCapture;
import uk.ac.ebi.subs.repository.model.templates.NoOpCapture;
import uk.ac.ebi.subs.repository.model.templates.Template;

import java.io.IOException;

@RunWith(JUnit4.class)
public class TemplateSerialisationTest {

    private Template template;

    private ObjectMapper objectMapper;

    @Before
    public void buildUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        template = new Template();

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
