package uk.ac.ebi.subs.repository.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.subs.repository.model.templates.Template;

import java.util.Date;

@Data
@Document
public class Checklist implements Identifiable<String>, Persistable<String> {

    @Id
    private String id;

    private String dataTypeId;

    private String displayName;
    private String description;

    @JsonRawValue
    private String validationSchema;

    @JsonSetter("validationSchema")
    public void setValidationSchema(JsonNode validationSchema) {
        this.validationSchema = validationSchema.toString();
    }

    private Template spreadsheetTemplate;

    private Boolean outdated;

    private Long version = 1L;

    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
