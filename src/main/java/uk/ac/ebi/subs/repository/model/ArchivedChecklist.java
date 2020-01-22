package uk.ac.ebi.subs.repository.model;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Data
@Document
// By default spring data mongo auditing copies root object's audit info to encapsulated objects.
// i.e. upon saving, spring copies value of 'createdDate' of ArchiveChecklist object into 'checklist.createdDate' and 'checklist.lastModifiedDate'
// Implementing the Auditable interface gives that control to us thus letting us avoid that.
public class ArchivedChecklist<U> implements Persistable<String>, Auditable<U, String> {

    private String id;

    private Checklist checklist;

    private DateTime createdDate;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    @Override
    public U getCreatedBy() {
        return null;
    }

    @Override
    public void setCreatedBy(U createdBy) {    }

    @Override
    public U getLastModifiedBy() {
        return null;
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {

    }

    @Override
    public DateTime getLastModifiedDate() {
        return null;
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {

    }
}
