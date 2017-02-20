package uk.ac.ebi.subs.repository.model;


import uk.ac.ebi.subs.data.submittable.Submittable;

import java.util.Date;


public interface StoredSubmittable extends Submittable {

    Submission getSubmission();
    void setSubmission(Submission submission);

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

}

