package uk.ac.ebi.subs.repository.model;

import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.data.submittable.Submittable;
import uk.ac.ebi.subs.validator.data.ValidationResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public interface StoredSubmittable extends Submittable, Identifiable<String> {

    ValidationResult getValidationResult();

    void setValidationResult(ValidationResult validationResult);

    Submission getSubmission();

    void setSubmission(Submission submission);

    ProcessingStatus getProcessingStatus();

    void setProcessingStatus(ProcessingStatus processingStatus);

    Long getVersion();

    void setVersion(Long version);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getLastModifiedDate();

    void setLastModifiedDate(Date lastModifiedDate);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getLastModifiedBy();

    void setLastModifiedBy(String lastModifiedBy);

    Stream<AbstractSubsRef> refs();

    DataType getDataType();

    void setDataType(DataType dataType);

    Checklist getChecklist();

    void setChecklist(Checklist checklist);

    void setReferences(Map<String, List<AbstractSubsRef>> references);


    Map<String, List<AbstractSubsRef>> getReferences();


}

