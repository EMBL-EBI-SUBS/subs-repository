package uk.ac.ebi.subs.model.templates;


import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.skyscreamer.jsonassert.JSONAssert;
import uk.ac.ebi.subs.repository.model.templates.DateFieldCapture;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class DateCaptureTest {

    DateFieldCapture dateCapture;

    final String dateColumnName = "release date";


    @Before
    public void buildUp() {
        dateCapture = DateFieldCapture.builder()
                .fieldName("releaseDate")
                .required(true)
                .displayName(dateColumnName)
                .build();
    }

    @Test
    public void test_iso_success() {
        List<String> header = Arrays.asList(dateColumnName);
        List<String> values = Arrays.asList("2018-10-26");

        JSONObject jsonActual = new JSONObject();
        int nextPosition = dateCapture.capture(0, header, values, jsonActual);

        JSONObject jsonExpected = new JSONObject("{\"releaseDate\": \"2018-10-26\"}");


        JSONAssert.assertEquals(jsonExpected, jsonActual, true);


        Assert.assertEquals(1, nextPosition);
    }

    @Test
    public void test_nonISO_success() {
        List<String> header = Arrays.asList(dateColumnName);
        List<String> values = Arrays.asList("26/10/2018");

        JSONObject jsonActual = new JSONObject();
        int nextPosition = dateCapture.capture(0, header, values, jsonActual);

        JSONObject jsonExpected = new JSONObject("{\"releaseDate\": \"2018-10-26\"}");


        JSONAssert.assertEquals(jsonExpected, jsonActual, true);


        Assert.assertEquals(1, nextPosition);
    }

    @Test
    public void test_unclear_novalue() {
        List<String> header = Arrays.asList(dateColumnName);
        List<String> values = Arrays.asList("121212");

        JSONObject jsonActual = new JSONObject();
        int nextPosition = dateCapture.capture(0, header, values, jsonActual);

        JSONObject jsonExpected = new JSONObject("{}");


        JSONAssert.assertEquals(jsonExpected, jsonActual, true);


        Assert.assertEquals(1, nextPosition);
    }
}
