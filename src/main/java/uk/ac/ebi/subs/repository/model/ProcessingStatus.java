package uk.ac.ebi.subs.repository.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.subs.data.status.ProcessingStatusEnum;

import java.util.Date;

@CompoundIndexes({
        @CompoundIndex(background = true, name = "submissionId_submittableId", def = "{ 'submissionId': 1, 'submittableId': 1}"),
        @CompoundIndex(background = true, name = "submissionId_status", def = "{ 'submissionId': 1, 'status': 1}"),
        @CompoundIndex(background = true, name = "submittableId", def = "{ 'submittableId': 1}")
})
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document
public class ProcessingStatus extends uk.ac.ebi.subs.data.status.ProcessingStatus implements Identifiable<String> {

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
    @DBRef
    private DataType dataType;
    private String accession;
    private String message;
    private String archive;
    private String alias;

    public ProcessingStatus() {
    }
    public ProcessingStatus(ProcessingStatusEnum statusEnum) {
        super(statusEnum);
    }

    public static ProcessingStatus createForSubmittable(StoredSubmittable storedSubmittable) {
        ProcessingStatus processingStatus = new ProcessingStatus(ProcessingStatusEnum.Draft);

        processingStatus.copyDetailsFromSubmittable(storedSubmittable);

        return processingStatus;
    }

    public void copyDetailsFromSubmittable(StoredSubmittable storedSubmittable) {
        if (storedSubmittable.getSubmission() != null)
            this.setSubmissionId(storedSubmittable.getSubmission().getId());

        this.setSubmittableId(storedSubmittable.getId());
        this.setSubmittableType(storedSubmittable.getClass().getSimpleName());
        this.setDataType(storedSubmittable.getDataType());

        this.setAlias(storedSubmittable.getAlias());

    }

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

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
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
