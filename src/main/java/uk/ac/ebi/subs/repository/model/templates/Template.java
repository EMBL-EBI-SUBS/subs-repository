package uk.ac.ebi.subs.repository.model.templates;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@Document
public class Template implements Identifiable<String> {

    @Id
    private String id;

    @NonNull
    @Indexed
    private String name;

    @Indexed
    private String targetType;

    @Builder.Default
    private Map<String, Capture> columnCaptures = new CaseInsensitiveMap();

    private Capture defaultCapture;

    public Template add(String columnName, Capture capture) {
        columnCaptures.put(columnName, capture);
        return this;
    }

    @Version
    private Long version;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;

    private static class CaseInsensitiveMap extends LinkedHashMap<String, Capture> {

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

        private static String mungeKey(Object key) {
            return key.toString().toLowerCase();
        }


    }
}
