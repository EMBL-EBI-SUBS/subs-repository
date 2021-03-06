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
 * Capture a column into a JSON structure that matches the Attribute component. Columns called 'units' or 'terms' to the
 * immediate right of the primary column will be added to the attribute structure
 * @see uk.ac.ebi.subs.data.component.Attribute
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AttributeCapture implements Capture {

    private static final String ATTRIBUTES_FIELD_NAME = "attributes";
    private static final String UNITS_FIELD_NAME = "units";
    private static final String TERMS_FIELD_NAME = "terms";
    private static final String UNITS_COLUMN_NAME = "units";
    private static final String TERMS_COLUMN_NAME = "terms";
    private static final String URL_FIELD_NAME = "url";
    private String displayName;
    @Builder.Default
    private boolean required = false;
    @Builder.Default
    private boolean allowUnits = true;
    @Builder.Default
    private boolean allowTerms = true;

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        position++;
        ListIterator<String> headerIterator = headers.listIterator(position);

        while (headerIterator.hasNext()) {
            position = headerIterator.nextIndex();
            String header = headerIterator.next().trim().toLowerCase();

            if (allowUnits && header.toLowerCase().equals(UNITS_COLUMN_NAME)) {

                NoOpCapture unitsCapture = NoOpCapture.builder()
                        .displayName(UNITS_COLUMN_NAME)
                        .build();

                unitsCapture.setCaptureInList(position, captures, header);


            } else if (allowTerms && header.toLowerCase().equals(TERMS_COLUMN_NAME)) {

                NoOpCapture termsCapture = NoOpCapture.builder()
                        .displayName(TERMS_COLUMN_NAME)
                        .build();

                termsCapture.setCaptureInList(position, captures, header);

            } else {
                return position;
            }
        }

        return ++position;
    }

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        JSONObject attributes = JsonUtils.ensureObject(document, ATTRIBUTES_FIELD_NAME);

        String name = headers.get(position);
        JSONArray valuesArray = JsonUtils.ensureArray(attributes, name);

        JSONObject attribute = new JSONObject();
        valuesArray.put(attribute);

        String value = values.get(position);
        if (value != null && !value.isEmpty()) {
            attribute.put("value", value);
        }

        return parseDependentColumns(position, headers, values, attribute);
    }

    @Override
    public List<String> additionalExpectedColumnHeaders() {
        List<String> columnHeaders = new ArrayList<>();

        if (allowUnits) {
            columnHeaders.add(UNITS_COLUMN_NAME);
        }
        if (allowTerms) {
            columnHeaders.add(TERMS_COLUMN_NAME);
        }

        return columnHeaders;
    }

    private int parseDependentColumns(int position, List<String> headers, List<String> values, JSONObject attribute) {
        position++;

        while (position < headers.size()) {
            String header = headers.get(position).trim().toLowerCase();
            String value = values.get(position);

            if (header.toLowerCase().equals(UNITS_COLUMN_NAME)) {
                attribute.put(UNITS_FIELD_NAME, value);
            } else if (header.toLowerCase().equals(TERMS_COLUMN_NAME)) {
                JSONArray terms = JsonUtils.ensureArray(attribute, TERMS_FIELD_NAME);
                JSONObject term = new JSONObject();
                term.put(URL_FIELD_NAME, value);
                terms.put(term);
            } else {
                return position;
            }

            position++;
        }

        return position;
    }

}
