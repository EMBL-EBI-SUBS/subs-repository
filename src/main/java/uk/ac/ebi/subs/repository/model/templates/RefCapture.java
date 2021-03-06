package uk.ac.ebi.subs.repository.model.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Capture a cell value to the alias field in a JSON structure conforming to the AbstractSubsRef component
 *
 * @see uk.ac.ebi.subs.data.component.AbstractSubsRef
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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

        if (value != null && !value.isEmpty()) {
            addRef(value, document);
        }
        return ++position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        return ++position;
    }


    private void addRef(String value, JSONObject document) {
        if (asList) {
            addListRef(value, document);
        } else {
            addSingleRef(value, document);
        }

    }

    private void addSingleRef(String value, JSONObject document) {
        document.put(refKey, ref(value));
    }

    private void addListRef(String value, JSONObject document) {
        JSONArray array = JsonUtils.ensureArray(document, refKey);
        array.put(ref(value));
    }

    private JSONObject ref(String value) {
        JSONObject ref = new JSONObject();
        ref.put("alias", value);
        return ref;
    }

}
