package uk.ac.ebi.subs.repository.model.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Map a cell from a spreadsheet to a JSON document matching the File component
 * The primary column will provide the file name, it is also possible to have secondary columns for a label or type.
 * You can also set default values for label or type.
 *
 * @see uk.ac.ebi.subs.data.component.File
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileCapture implements Capture {


    private static final String LABEL_COLUMN_NAME = "label";
    private static final String TYPE_COLUMN_NAME = "type";
    private static final String FILES_ATTRIBUTE_NAME = "files";
    private static final String NAME_FIELD_NAME = "name";
    private static final String LABEL_FIELD_NAME = "label";
    private static final String TYPE_FIELD_NAME = "type";
    private String displayName;
    @Builder.Default
    private boolean required = false;
    @Builder.Default
    private boolean allowLabel = false;
    @Builder.Default
    private boolean allowType = false;
    @Builder.Default
    private String defaultLabel = null;
    @Builder.Default
    private String defaultType = null;

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        JSONArray files = JsonUtils.ensureArray(document, FILES_ATTRIBUTE_NAME);

        JSONObject file = new JSONObject();
        files.put(file);

        String value = values.get(position);
        file.put(NAME_FIELD_NAME, value);

        return parseDependentColumns(position, headers, values, file);
    }

    private int parseDependentColumns(int position, List<String> headers, List<String> values, JSONObject file) {
        position++;

        while (position < headers.size()) {
            String header = headers.get(position).trim().toLowerCase();
            String value = values.get(position);

            if (allowLabel && header.toLowerCase().equals(LABEL_COLUMN_NAME)) {
                file.put(LABEL_FIELD_NAME, value);
            } else if (allowType && header.toLowerCase().equals(TYPE_COLUMN_NAME)) {
                file.put(TYPE_FIELD_NAME, value);
            } else {
                break;
            }

            position++;
        }

        if (defaultLabel != null && !file.has(LABEL_FIELD_NAME)) {
            file.put(LABEL_FIELD_NAME, defaultLabel);
        }
        if (defaultType != null && !file.has(TYPE_FIELD_NAME)) {
            file.put(TYPE_FIELD_NAME, defaultType);
        }

        return position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        position++;
        ListIterator<String> headerIterator = headers.listIterator(position);

        while (headerIterator.hasNext()) {
            position = headerIterator.nextIndex();
            String header = headerIterator.next().trim().toLowerCase();

            if (allowLabel && header.toLowerCase().equals(LABEL_COLUMN_NAME)) {

                NoOpCapture unitsCapture = NoOpCapture.builder()
                        .displayName(this.getDisplayName().concat(" label"))
                        .build();

                unitsCapture.setCaptureInList(position, captures, header);


            } else if (allowType && header.toLowerCase().equals(TYPE_COLUMN_NAME)) {

                NoOpCapture termsCapture = NoOpCapture.builder()
                        .displayName(this.getDisplayName().concat(" type"))
                        .build();

                termsCapture.setCaptureInList(position, captures, header);

            } else {
                return position;
            }
        }

        return ++position;
    }

    @Override
    public List<String> additionalExpectedColumnHeaders() {
        List<String> columnHeaders = new ArrayList<>();

        if (allowLabel) {
            columnHeaders.add(LABEL_COLUMN_NAME);
        }
        if (allowType) {
            columnHeaders.add(TYPE_COLUMN_NAME);
        }

        return columnHeaders;
    }
}
