package uk.ac.ebi.subs.model.templates;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.skyscreamer.jsonassert.JSONAssert;
import uk.ac.ebi.subs.repository.model.templates.FileCapture;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class FileCaptureTest {

    FileCapture fileCapture1, fileCapture2;

    final String filenameColumnHeader = "file name";
    final String labelColumnHeader = "label";
    final String typeColumnHeader = "type";
    final String fileName = "kafka.mp4";
    final String label = "video";
    final String type = "MP4";

    @Before
    public void buildUp() {
        fileCapture1 = FileCapture.builder()
                .allowLabel(true)
                .allowType(true)
                .build();

        fileCapture2 = FileCapture.builder()
                .allowLabel(false)
                .allowType(false)
                .defaultLabel(null)
                .build();
    }

    @Test
    public void test_capture() {
        List<String> header = Arrays.asList(filenameColumnHeader, labelColumnHeader, typeColumnHeader);
        List<String> values = Arrays.asList(fileName, label, type);

        JSONObject jsonActual = new JSONObject();
        int nextPosition = fileCapture1.capture(0, header, values, jsonActual);

        JSONObject jsonExpected = new JSONObject("{\n" +
                "  \"files\": [{\n" +
                "    \"name\": \"kafka.mp4\",\n" +
                "    \"label\": \"video\",\n" +
                "    \"type\": \"MP4\"\n" +
                "  }]\n" +
                "}");


        JSONAssert.assertEquals(jsonExpected, jsonActual, true);


        Assert.assertEquals(3, nextPosition);
    }

    @Test
    public void test_capture_with_default() {
        List<String> header = Arrays.asList(filenameColumnHeader);
        List<String> values = Arrays.asList(fileName);

        JSONObject jsonActual = new JSONObject();
        int nextPosition = fileCapture2.capture(0, header, values, jsonActual);

        JSONObject jsonExpected = new JSONObject("{\n" +
                "  \"files\": [{\n" +
                "    \"name\": \"kafka.mp4\",\n" +
                "  }]\n" +
                "}");


        JSONAssert.assertEquals(jsonExpected, jsonActual, true);


        Assert.assertEquals(1, nextPosition);
    }


}
