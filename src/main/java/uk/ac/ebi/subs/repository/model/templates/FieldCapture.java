package uk.ac.ebi.subs.repository.model.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONObject;

import java.util.List;

/**
 * Map a cell value to a single top level field in a JSON document
 */
@Builder(toBuilder = true)
@Data
public class FieldCapture implements Capture {

    @NonNull
    private String fieldName;
    @Builder.Default
    private JsonFieldType fieldType = JsonFieldType.String;
    @Builder.Default
    private boolean required = false;
    private String displayName;

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        String value = values.get(position);

        if (value != null && !value.isEmpty()) {
            fieldType.addValueToDocument(fieldName, value, document);
        }
        return ++position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        return ++position;
    }
}
