package uk.ac.ebi.subs.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.validator.data.ValidationResult;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@CompoundIndexes({
        @CompoundIndex(background = true, name = "team_alias", def = "{ 'team.name': 1, 'alias': 1 }"),
        @CompoundIndex(background = true, name = "accession", def = "{ 'accession': 1}"),
        @CompoundIndex(background = true, name = "submission", def = "{ 'submission': 1}")
})
@Document
public class EgaDataset extends uk.ac.ebi.subs.data.submittable.EgaDataset implements StoredSubmittable {

    @Override
    public Stream<AbstractSubsRef> refs() {

        Stream<AbstractSubsRef> analysisStream = (this.getAnalysisRefs() == null)
                ? Stream.empty() : this.getAnalysisRefs().stream().map(ref -> (AbstractSubsRef) ref);

        Stream<AbstractSubsRef> dataStream = (this.getDataRefs() == null)
                ? Stream.empty() : this.getDataRefs().stream().map(ref -> (AbstractSubsRef) ref);

        Stream<AbstractSubsRef> policyRefStream = (this.getEgaDacPolicyRef() == null)
                ? Stream.empty() : Stream.of(this.getEgaDacPolicyRef());

        return Arrays.asList(analysisStream,dataStream,policyRefStream)
                .stream()
                .flatMap(stream -> stream);
    }

    @DBRef
    private ProcessingStatus processingStatus;
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
    @DBRef
    private Submission submission;
    @DBRef
    private ValidationResult validationResult;

    @DBRef
    private DataType dataType;
    @DBRef
    private Checklist checklist;

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public Checklist getChecklist() {
        return checklist;
    }

    @Override
    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    @Override
    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    @Override
    public void setProcessingStatus(ProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    private Map<String, List<AbstractSubsRef>> references;

    @JsonIgnore
    public void setReferences(Map<String, List<AbstractSubsRef>> references) {
        this.references = references;
    }

    public Map<String, List<AbstractSubsRef>> getReferences() {
        return references;
    }
}
