package uk.ac.ebi.subs.repository.model;


import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;


@CompoundIndexes({
        @CompoundIndex(name = "domain_alias", def = "{ 'domain.name': 1, 'alias': 1 }"),
        @CompoundIndex(name = "accession", def = "{ 'accession': 1}"),
        @CompoundIndex(name = "submissionId_status", def = "{ 'submission.$id': 1, 'status': 1}")
})
@Document
public class EgaDac extends uk.ac.ebi.subs.data.submittable.EgaDac implements StoredSubmittable {

    @Version
    private Long version;
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;
    @CreatedBy private String createdBy;
    @LastModifiedBy private String lastModifiedBy;

    @DBRef
    private Submission submission;

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
}
