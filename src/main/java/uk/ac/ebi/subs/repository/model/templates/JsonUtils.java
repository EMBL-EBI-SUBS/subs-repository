package uk.ac.ebi.subs.repository.model.templates;

import org.json.JSONArray;
import org.json.JSONObject;

class JsonUtils {

    static JSONObject ensureObject(JSONObject document, String objectFieldName) {

        if (!document.has(objectFieldName)) {
            document.put(objectFieldName, new JSONObject());
        }

        return document.getJSONObject(objectFieldName);

    }

    static JSONArray ensureArray(JSONObject document, String arrayFieldName) {

        if (!document.has(arrayFieldName)) {
            document.put(arrayFieldName, new JSONArray());
        }

        return document.getJSONArray(arrayFieldName);

    }
}
