package uk.ac.ebi.subs.model.templates;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.repository.model.templates.RefCapture;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class RefCaptureTest {

    RefCapture singleRefCapture;

    RefCapture refListCapture;

    final String columnHeader = "ref alias";
    final String firstAlias = "alias1";
    final String secondAlias = "alias2";

    final String aliasKey = "alias";
    final String singleRefKey = "ref";
    final String refListKey = "refs";

    @Before public void buildUp() {
        singleRefCapture = RefCapture.builder()
                .refKey("ref")
                .build();

        refListCapture = RefCapture.builder()
                .refKey("refs")
                .asList(true)
                .build();
    }

    @Test public void single_capture(){

        List<String> header = Arrays.asList(columnHeader);
        List<String> values = Arrays.asList(firstAlias);


        JSONObject jsonActual = new JSONObject();
        int nextPosition = singleRefCapture.capture(0,header,values,jsonActual);

        Assert.assertEquals(1, nextPosition);

        JSONObject jsonExpected = new JSONObject();
        JSONObject refObject = new JSONObject();
        refObject.put(aliasKey,firstAlias);
        jsonExpected.put(singleRefKey,refObject);

        JSONAssert.assertEquals(jsonExpected,jsonActual,true);
    }

    @Test public void list_capture(){
        List<String> header = Arrays.asList(columnHeader,columnHeader);
        List<String> values = Arrays.asList(firstAlias,secondAlias);

        JSONObject jsonActual = new JSONObject();
        int nextPosition = refListCapture.capture(0,header,values,jsonActual);
        Assert.assertEquals(1, nextPosition);
        nextPosition = refListCapture.capture(nextPosition,header,values,jsonActual);
        Assert.assertEquals(2, nextPosition);

        JSONObject jsonExpected = new JSONObject();
        JSONArray refs = new JSONArray();
        jsonExpected.put(refListKey,refs);

        for (String alias : values){
            JSONObject refObject = new JSONObject();
            refObject.put(aliasKey,alias);
            refs.put(refObject);
        }
        
        JSONAssert.assertEquals(jsonExpected,jsonActual,true);
    }

}
