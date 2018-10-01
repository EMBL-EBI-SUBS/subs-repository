package uk.ac.ebi.subs.model.templates;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.repository.model.templates.SingleRefCapture;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class SingleRefCaptureTest {

    SingleRefCapture singleRefCapture;

    @Before public void buildUp() {
        singleRefCapture = SingleRefCapture.builder()
                .refKey("ref")
                .build();
    }

    @Test public void capture(){

        List<String> header = Arrays.asList("ref alias");
        List<String> values = Arrays.asList("alias1");

        JSONObject jsonActual = new JSONObject();


        JSONObject jsonExpected = new JSONObject();
        JSONObject refObject = new JSONObject();
        refObject.put("alias","alias1");
        jsonExpected.put("ref",refObject);

        int nextPosition = singleRefCapture.capture(0,header,values,jsonActual);


        Assert.assertEquals(1, nextPosition);

        JSONAssert.assertEquals(jsonExpected,jsonActual,true);
    }

}
