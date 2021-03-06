package uk.ac.ebi.subs.repository.model.templates;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A description of requirements for a spreadsheet and mappings from a spreadsheet to a JSON document
 */
@Data
public class Template {

    private Map<String, Capture> columnCaptures = new CaseInsensitiveMap();

    private Capture defaultCapture;

    public Template add(String columnName, Capture capture) {
        columnCaptures.put(columnName, capture);
        return this;
    }

    private static class CaseInsensitiveMap extends LinkedHashMap<String, Capture> {

        private static String mungeKey(Object key) {
            return key.toString().toLowerCase();
        }

        public Capture get(Object key) {
            return super.get(mungeKey(key));
        }

        public Capture put(String key, Capture value) {
            return super.put(mungeKey(key), value);
        }

        @Override
        public boolean containsKey(Object key) {
            return super.containsKey(mungeKey(key));
        }


    }
}
