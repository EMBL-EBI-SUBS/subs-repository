package uk.ac.ebi.subs.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.data.component.Contact;
import uk.ac.ebi.subs.repository.model.Submission;

import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
public class SubmissionSerialisationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialiseDerserialiseContactThroughSub() throws IOException {
        Contact arbitaryData = new Contact();
        arbitaryData.setFirstName("Bob");
        arbitaryData.setRoles(Arrays.asList("Jam", "Cinnamon"));

        String arbitaryJson = objectMapper.writeValueAsString(arbitaryData);

        Submission s = new Submission();
        s.setUiData(arbitaryJson);

        String serialisedSubmission = objectMapper.writeValueAsString(s);

        Submission deserialisedSubmission = objectMapper.readValue(serialisedSubmission,Submission.class);

        String arbitaryJsonAfterDeser = deserialisedSubmission.getUiData();

        Contact deserialisedContact = objectMapper.readValue(arbitaryJsonAfterDeser,Contact.class);

        Assert.assertEquals(arbitaryData,deserialisedContact);
    }
}
