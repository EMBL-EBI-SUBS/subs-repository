package uk.ac.ebi.subs.model.templates;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.repository.model.templates.FileCapture;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileCaptureTest {

    FileCapture fileCapture;

    final String filenameColumnHeader = "file name";
    final String labelColumnHeader = "label";
    final String typeColumnHeader = "type";
    final String fileName = "kafka.mp4";
    final String label = "video";
    final String type = "MP4";

    @Before
    public void buildUp() {
        fileCapture = FileCapture.builder()
                .allowLabel(true)
                .allowType(true)
                .build();
    }

    @Test
    public void test_capture() {
        List<String> header = Arrays.asList(filenameColumnHeader, labelColumnHeader, typeColumnHeader);
        List<String> values = Arrays.asList(fileName, label, type);

        JSONObject jsonActual = new JSONObject();
        int nextPosition = fileCapture.capture(0, header, values, jsonActual);

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


}
