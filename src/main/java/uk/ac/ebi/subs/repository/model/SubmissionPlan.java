package uk.ac.ebi.subs.repository.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.util.List;

@Data
@Document
public class SubmissionPlan implements Identifiable<String> {

    @Id
    private String id;

    private String displayName;

    private String description;

    private List<String> dataTypeIds;

    private boolean enabled;
}
