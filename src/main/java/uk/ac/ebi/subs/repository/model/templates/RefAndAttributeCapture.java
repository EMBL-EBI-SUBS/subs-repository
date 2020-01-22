package uk.ac.ebi.subs.repository.model.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Capture cell values to meet the SampleUse or ProtocolUse structures
 * These take the form of a reference, and Attributes
 *
 * This capture will capture the reference alias, and named attributes (determined through configuration)
 *
 * @see uk.ac.ebi.subs.data.component.SampleUse
 * @see uk.ac.ebi.subs.data.component.ProtocolUse
 *
 * @see uk.ac.ebi.subs.data.component.AbstractSubsRef
 * @see uk.ac.ebi.subs.data.component.Attribute
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RefAndAttributeCapture implements Capture {

    @NonNull
    private String key;

    @NonNull
    private String refKey;

    private String displayName;

    @Builder.Default
    private boolean required = false;

    @Singular
    private Map<String, AttributeCapture> attributeCaptures = new LinkedHashMap<>();

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject rowDocument) {
        JSONArray targetArray = JsonUtils.ensureArray(rowDocument, key);
        JSONObject targetDocument = new JSONObject();
        targetArray.put(targetDocument);

        RefCapture refCapture = RefCapture.builder()
                .refKey(refKey)
                .asList(false)
                .build();

        position = refCapture.capture(position,headers,values,targetDocument);

        for (Map.Entry<String,AttributeCapture> entry : attributeCaptures.entrySet()){
            String primaryColumnName = entry.getKey();

            AttributeCapture attributeCapture = entry.getValue();
            attributeCapture.setDisplayName(primaryColumnName);

            position = attributeCapture.capture(position,headers,values,targetDocument);
        }


        return position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        ++position;

        for (Map.Entry<String,AttributeCapture> entry : attributeCaptures.entrySet()){
            String primaryColumnName = entry.getKey();

            AttributeCapture attributeCapture = entry.getValue();
            attributeCapture.setDisplayName(primaryColumnName);

            position = attributeCapture.map(position,captures,headers);
        }


        return position;
    }

    @Override
    public List<String> additionalExpectedColumnHeaders() {
        List<String> extraHeaders = new LinkedList<>();

        for (Map.Entry<String,AttributeCapture> entry : attributeCaptures.entrySet()){
            extraHeaders.add(entry.getKey());
            extraHeaders.addAll(entry.getValue().additionalExpectedColumnHeaders());
        }

        return extraHeaders;
    }

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }




}
