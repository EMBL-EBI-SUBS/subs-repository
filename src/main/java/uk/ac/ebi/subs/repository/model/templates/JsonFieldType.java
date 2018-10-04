package uk.ac.ebi.subs.repository.model.templates;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dave on 18/10/2017.
 */
public enum JsonFieldType {

    String,
    IntegerNumber,
    FloatNumber,
    Boolean;


    private static final Map<String, Boolean> booleanTextValues;

    static {
        Map<String, Boolean> map = new HashMap<>();
        map.put("true", true);
        map.put("false", false);
        booleanTextValues = Collections.unmodifiableMap(map);
    }

    public void addValueToDocument(String fieldName, String value, JSONObject document) throws NumberFormatException {
        switch (this) {
            case String:
                document.put(fieldName, value);
                break;
            case IntegerNumber:
                long longVal = Long.parseLong(value);
                document.put(fieldName, longVal);
                break;
            case FloatNumber:
                double doubleVal = Double.parseDouble(value);
                document.put(fieldName, doubleVal);
                break;
            case Boolean:
                String lcValue = value.toLowerCase();
                Boolean boolVal = booleanTextValues.get(lcValue);
                document.put(fieldName, boolVal);
                break;
            default:
                throw new IllegalArgumentException("cannot add value for type: " + this.name());
        }

    }
}
