package uk.ac.ebi.subs.model.templates;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.skyscreamer.jsonassert.JSONAssert;
import uk.ac.ebi.subs.repository.model.templates.AttributeCapture;
import uk.ac.ebi.subs.repository.model.templates.Capture;
import uk.ac.ebi.subs.repository.model.templates.NoOpCapture;
import uk.ac.ebi.subs.repository.model.templates.RefAndAttributeCapture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class RefAndAttributeCaptureTest {

    RefAndAttributeCapture capture;

    List<String> headers;
    List<String> values;

    @Before
    public void buildUp() {
        this.capture = RefAndAttributeCapture
                .builder()
                .key("protocolUses")
                .refKey("protocolRef")
                .required(true)
                .attributeCapture(
                        "Post Extraction",
                        AttributeCapture.builder()
                                .allowTerms(true)
                                .allowUnits(false)
                                .required(false)
                                .build()
                )
                .attributeCapture(
                        "Extract Name",
                        AttributeCapture.builder()
                                .allowTerms(false)
                                .allowUnits(false)
                                .required(true)
                                .build()
                )
                .attributeCapture(
                        "Extract time",
                        AttributeCapture.builder()
                                .allowTerms(false)
                                .allowUnits(true)
                                .required(false)
                                .build()
                )
                .build();

        this.headers = Arrays.asList(
                "Extraction Protocol Alias",
                "Post Extraction",
                "terms",
                "Extract Name",
                "Extract time",
                "units"
        );

        this.values = Arrays.asList(
                "myProtocolAlias",
                "95:5 (v/v) water:acetonitrile and 0.05 µg/ml deuterated biotin",
                "http://www.ebi.ac.uk/efo/EFO_0000494",
                "extract_47",
                "14.3",
                "hour"
        );
    }

    @Test
    public void testMapping() {
        int startPosition = 0;
        List<Capture> captures = new ArrayList<>(Collections.nCopies(headers.size(), null));

        int endPosition = capture.map(startPosition, captures, this.headers);

        Assert.assertEquals(6, endPosition);

        List<Capture> expectedCaptures = Arrays.asList(
                capture,
                capture.getAttributeCaptures().get(headers.get(1)),
                NoOpCapture.builder().displayName(headers.get(2)).build(),
                capture.getAttributeCaptures().get(headers.get(3)),
                capture.getAttributeCaptures().get(headers.get(4)),
                NoOpCapture.builder().displayName(headers.get(5)).build()
        );

        Assert.assertEquals(expectedCaptures, captures);

    }

    @Test
    public void testCapture() {

        JSONObject jsonExpected = new JSONObject("{\n" +
                "  \"protocolUses\": [\n" +
                "    {\n" +
                "      \"protocolRef\": {\n" +
                "        \"alias\": \"myProtocolAlias\"\n" +
                "      },\n" +
                "      \"attributes\": {\n" +
                "        \"Post Extraction\": [\n" +
                "          {\n" +
                "            \"value\": \"95:5 (v/v) water:acetonitrile and 0.05 µg/ml deuterated biotin\",\n" +
                "            \"terms\": [\n" +
                "              {\n" +
                "                \"url\": \"http://www.ebi.ac.uk/efo/EFO_0000494\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"Extract Name\": [\n" +
                "          {\n" +
                "            \"value\": \"extract_47\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"Extract time\": [\n" +
                "          {\n" +
                "            \"value\": \"14.3\",\n" +
                "            \"units\": \"hour\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}");

        JSONObject jsonActual = new JSONObject();

        int startPosition = 0;

        int endPosition = capture.capture(startPosition, headers, values, jsonActual);

        JSONAssert.assertEquals(jsonExpected, jsonActual, true);
        Assert.assertEquals(6, endPosition);

    }

    @Test
    public void testExpectedColumns() {
        List<String> actualExtraColumnHeaders = capture.additionalExpectedColumnHeaders();

        Assert.assertEquals(headers.subList(1, 6), actualExtraColumnHeaders);
    }


}
