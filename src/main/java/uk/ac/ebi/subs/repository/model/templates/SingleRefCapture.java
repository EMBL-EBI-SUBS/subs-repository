package uk.ac.ebi.subs.repository.model.templates;

import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class SingleRefCapture implements Capture {

    private String refKey;
    private String displayName;
    private boolean required;

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        String value = values.get(position);

        if (value != null) {
            addRef(value,document);
        }
        return ++position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position,captures,headers.get(position));

        return ++position;
    }

    @Override
    public List<String> expectedColumnHeaders() {
        return Collections.emptyList();
    }

    private void addRef(String value, JSONObject document){
        JSONObject  ref = new JSONObject();
        ref.put("alias",value);
        document.put(refKey,ref);
    }


}
