package uk.ac.ebi.subs.repository.model.templates;

import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

import java.util.List;

/**
 * This satisfies the Capture interface, but doesn't actually change the document at all. It's useful to
 * fill in the column list when building up a spreadsheet template
 */
@Data
@Builder(toBuilder = true)
public class NoOpCapture implements Capture {

    private String displayName;


    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        return ++position;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {
        return 0;
    }

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {

    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
