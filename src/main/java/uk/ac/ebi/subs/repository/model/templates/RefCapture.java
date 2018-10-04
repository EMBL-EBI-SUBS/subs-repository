package uk.ac.ebi.subs.repository.model.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class RefCapture implements Capture {

    @NonNull
    private String refKey;

    private String displayName;

    @Builder.Default
    private boolean required = false;

    @Builder.Default
    private boolean asList = false;

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


    private void addRef(String value, JSONObject document){
        if (asList){
            addListRef(value,document);
        }
        else {
            addSingleRef(value,document);
        }

    }

    private void addSingleRef(String value, JSONObject document){
        document.put(refKey,ref(value));
    }

    private void addListRef(String value, JSONObject document){
        if (!document.has(refKey)){
            document.put(refKey,new JSONArray());
        }

        JSONArray array = document.getJSONArray(refKey);
        array.put(ref(value));
    }

    private JSONObject ref(String value){
        JSONObject  ref = new JSONObject();
        ref.put("alias",value);
        return ref;
    }

}
