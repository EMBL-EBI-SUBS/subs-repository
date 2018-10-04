package uk.ac.ebi.subs.repository.model.templates;

import lombok.Builder;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Data
@Builder(toBuilder = true)
public class SampleRelationshipCapture implements Capture {

    private static final String SAMPLE_RELATIONSHIP_ATTRIBUTE_NAME = "sampleRelationships";
    private static final String RELATIONSHIP_NATURE_FIELD_NAME = "relationshipNature";
    private static final String ALIAS_FIELD_NAME = "alias";
    private static final String RELATIONSHIP_NATURE_COLUMN_NAME = "relationship nature";

    private String displayName;

    @Builder.Default
    private boolean required = false;

    @Builder.Default
    private boolean allowRelationshipNature = false;

    @Builder.Default
    private String defaultRelationship = null;


    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        JSONArray relationships = ensureArray(document, SAMPLE_RELATIONSHIP_ATTRIBUTE_NAME);

        JSONObject relationship = null;

        String value = values.get(position);
        if (value != null && !value.isEmpty()) {
            relationship = new JSONObject();
            relationships.put(relationship);
            relationship.put(ALIAS_FIELD_NAME, value);
        }

        return parseDependentColumns(position, headers, values, relationship);
    }

    private int parseDependentColumns(int position, List<String> headers, List<String> values, JSONObject relationship) {
        position++;

        while (position < headers.size()) {
            String header = headers.get(position).trim().toLowerCase();
            String value = values.get(position);

            if (relationship != null && allowRelationshipNature && header.toLowerCase().equals(RELATIONSHIP_NATURE_COLUMN_NAME)) {
                relationship.put(RELATIONSHIP_NATURE_FIELD_NAME, value);
            } else {
                return position;
            }

            position++;
        }

        if (relationship != null &&  defaultRelationship != null && !relationship.has(RELATIONSHIP_NATURE_FIELD_NAME)) {
            relationship.put(RELATIONSHIP_NATURE_FIELD_NAME, defaultRelationship);
        }

        return position;
    }


    private JSONArray ensureArray(JSONObject document, String arrayFieldName) {

        if (!document.has(arrayFieldName)) {
            document.put(arrayFieldName, new JSONArray());
        }

        return document.getJSONArray(arrayFieldName);

    }


    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        position++;
        ListIterator<String> headerIterator = headers.listIterator(position);

        while (headerIterator.hasNext()) {
            position = headerIterator.nextIndex();
            String header = headerIterator.next().trim().toLowerCase();

            if (allowRelationshipNature && header.toLowerCase().equals(RELATIONSHIP_NATURE_COLUMN_NAME)) {

                NoOpCapture unitsCapture = NoOpCapture.builder()
                        .displayName(this.getDisplayName().concat(" relationship nature"))
                        .build();

                unitsCapture.setCaptureInList(position, captures, header);


            } else {
                return position;
            }
        }

        return ++position;
    }

    @Override
    public List<String> additionalExpectedColumnHeaders() {
        List<String> columnHeaders = new ArrayList<>();

        if (allowRelationshipNature) {
            columnHeaders.add(RELATIONSHIP_NATURE_COLUMN_NAME);
        }


        return columnHeaders;
    }
}
