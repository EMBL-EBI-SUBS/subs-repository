package uk.ac.ebi.subs.repository.model.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateFieldCapture implements Capture {

    @NonNull
    private String fieldName;

    private static final JsonFieldType fieldType = JsonFieldType.String;

    @Builder.Default
    private boolean required = false;
    private String displayName;

    @Override
    public Capture copy() {
        return this.toBuilder().build();
    }

    private final static DateFormat[] inputDateFormats = intialiseInputDateFormats();

    private static DateFormat[] intialiseInputDateFormats() {
        DateFormat[] formats = {
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("yyyy/MM/dd"),
                new SimpleDateFormat("dd-MM-yyyy"),
                new SimpleDateFormat("dd/MM/yyyy")
        };

        for (DateFormat df : formats) {
            df.setLenient(false);
        }
        return formats;
    }

    private final static DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int capture(int position, List<String> headers, List<String> values, JSONObject document) {
        String value = values.get(position);

        String safeValue = sanitizeValue(value);

        if (safeValue != null && !safeValue.isEmpty()) {

            fieldType.addValueToDocument(fieldName, safeValue, document);
        }
        return ++position;
    }

    private String sanitizeValue(String value) {

        if (value == null) return null;

        value = value.trim();
        if (value.isEmpty()) return null;


        for (DateFormat df : inputDateFormats) {
            try {
                Date date = df.parse(value);
                return outputDateFormat.format(date);
            } catch (ParseException e) {
                //this is expected,
            }
        }

        return null;
    }

    @Override
    public int map(int position, List<Capture> captures, List<String> headers) {

        this.setCaptureInList(position, captures, headers.get(position));

        return ++position;
    }
}
