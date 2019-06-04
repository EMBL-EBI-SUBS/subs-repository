package uk.ac.ebi.subs.repository.model.accession;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@EqualsAndHashCode(callSuper = true)
public class AccessionIdWrapper extends uk.ac.ebi.subs.data.accession.AccessionIdWrapper {

    @Id
    private String id;

    private LocalDateTime messageSentDate;
}
