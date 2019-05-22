package uk.ac.ebi.subs.repository.model.accession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@EqualsAndHashCode
public class AccessionIdWrapper {

    @Id
    private String id;

    private String submissionId;
    private String bioStudiesAccessionId;
    private List<String> bioSamplesAccessionIds;
}
