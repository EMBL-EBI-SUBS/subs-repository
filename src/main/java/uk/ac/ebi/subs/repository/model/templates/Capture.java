package uk.ac.ebi.subs.repository.model.templates;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttributeCapture.class, name = "attribute"),
        @JsonSubTypes.Type(value = FieldCapture.class, name = "field"),
        @JsonSubTypes.Type(value = NoOpCapture.class, name = "noop"),
        @JsonSubTypes.Type(value = RefCapture.class, name = "ref"),
        @JsonSubTypes.Type(value = FileCapture.class, name = "file"),
        @JsonSubTypes.Type(value = SampleRelationshipCapture.class, name = "sampleRelationship"),
        @JsonSubTypes.Type(value = RefAndAttributeCapture.class, name = "refAndAttributes")
})
public interface Capture {
    int capture(int position, List<String> headers, List<String> values, JSONObject document);
    int map(int position, List<Capture> captures, List<String> headers);

    Capture copy();
    String getDisplayName();
    void setDisplayName(String displayName);

    boolean isRequired();

    default void setCaptureInList(int position, List<Capture> captures, String header){
        Capture cap = this;
        captures.set(position,cap);
    }
    /**
     *
     * @return a list of column headers, in addition to the displayName. This is useful for captures that take multiple
     * columns
     */
    default List<String> additionalExpectedColumnHeaders() {
        return Collections.emptyList();
    }
}
