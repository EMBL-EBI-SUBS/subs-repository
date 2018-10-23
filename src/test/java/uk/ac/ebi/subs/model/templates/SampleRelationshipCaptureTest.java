package uk.ac.ebi.subs.model.templates;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.skyscreamer.jsonassert.JSONAssert;
import uk.ac.ebi.subs.repository.model.templates.SampleRelationshipCapture;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class SampleRelationshipCaptureTest {

    SampleRelationshipCapture sampleRelationshipCapture;

    final String sampleAliasColumnHeader = "sample alias";
    final String relationshipHeader = "relationship nature";

    final String sample = "sample1";
    final String relationship = "derived from";


    @Before
    public void buildUp() {
        sampleRelationshipCapture = SampleRelationshipCapture.builder()
                .allowRelationshipNature(true)
                .build();
    }

    @Test
    public void test_capture() {
        List<String> header = Arrays.asList(sampleAliasColumnHeader, relationshipHeader);
        List<String> values = Arrays.asList(sample, relationship);

        JSONObject jsonActual = new JSONObject();
        int nextPosition = sampleRelationshipCapture.capture(0, header, values, jsonActual);

        JSONObject jsonExpected = new JSONObject("{\n" +
                "  \"sampleRelationships\": [{\n" +
                "    \"alias\": \"sample1\",\n" +
                "    \"relationshipNature\": \"derived from\"\n" +
                "  }]\n" +
                "}");


        JSONAssert.assertEquals(jsonExpected, jsonActual, true);


        Assert.assertEquals(2, nextPosition);
    }
}
