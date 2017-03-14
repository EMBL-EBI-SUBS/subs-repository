package uk.ac.ebi.subs.repository.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.subs.data.status.ProcessingStatusEnum;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document
public class ProcessingStatus extends uk.ac.ebi.subs.data.status.ProcessingStatus implements Identifiable<String> {

    public ProcessingStatus() {
    }

    public static ProcessingStatus createForSubmittable(StoredSubmittable storedSubmittable) {
        ProcessingStatus processingStatus = new ProcessingStatus(ProcessingStatusEnum.Draft);

        processingStatus.setSubmissionId(storedSubmittable.getSubmission().getId());
        processingStatus.setSubmittableId(storedSubmittable.getId());
        processingStatus.setSubmittableType(storedSubmittable.getClass().getSimpleName());
        processingStatus.setArchive(storedSubmittable.getArchive().name());
        processingStatus.setAlias(storedSubmittable.getAlias());

        storedSubmittable.setProcessingStatus(processingStatus);

        return processingStatus;
    }

    public ProcessingStatus(ProcessingStatusEnum statusEnum) {
        super(statusEnum);
    }

    @Id
    private String id;

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

    @Indexed
    private String submissionId;


    private String submittableId;
    private String submittableType;

    private String accession;
    private String message;
    private String archive;
    private String alias;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubmittableId() {
        return submittableId;
    }

    public void setSubmittableId(String submittableId) {
        this.submittableId = submittableId;
    }

    public String getSubmittableType() {
        return submittableType;
    }

    public void setSubmittableType(String submittableType) {
        this.submittableType = submittableType;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
