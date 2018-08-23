package uk.ac.ebi.subs.repository.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.subs.repository.model.templates.Template;

@Data
@Document
public class Checklist implements Identifiable<String> {

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
    
}
