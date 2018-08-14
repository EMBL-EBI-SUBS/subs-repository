package uk.ac.ebi.subs.repository.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.subs.data.component.Archive;

import java.util.List;
import java.util.Set;

@Data
@Document
public class DataType {

    @Id
    private String id;

    private String displayNameSingular;
    private String displayNamePlural;
    private String description;

    @JsonRawValue
    private String validationSchema;

    @JsonSetter("validationSchema")
    public void setValidationSchema(JsonNode validationSchema) {
        this.validationSchema = validationSchema.toString();
    }

    private Set<RefRequirement> refRequirements;

    private List<String> requiredValidationAuthors;
    private List<String> optionalValidationAuthors;
    private String submittableClassName;

    private Archive archive;

    @Data
    public static class RefRequirement {
        private String refClassName; //e.g. StudyRef
        private String dataTypeIdForReferencedDocument; // e.g. enaStudy
        private Set<String> additionalRequiredValidationAuthors; //e.g. [ENA, taxonomy]
    }

}
