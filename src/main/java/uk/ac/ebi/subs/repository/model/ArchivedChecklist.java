package uk.ac.ebi.subs.repository.model;

import lombok.Data;
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
public class ArchivedChecklist<U> implements Identifiable<String>, Persistable<String>, Auditable<U, String, Instant> {

    private String id;

    private Checklist checklist;

    private Date createdDate;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }


    @Override
    public Optional<U> getCreatedBy() {
        return Optional.empty();
    }

    @Override
    public void setCreatedBy(U createdBy) {

    }

    @Override
    public void setCreatedDate(Instant creationDate) {
        this.createdDate = new Date(creationDate.toEpochMilli());
    }

    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.of(Instant.ofEpochMilli(this.createdDate.getTime()));
    }

    @Override
    public Optional<U> getLastModifiedBy() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {

    }

    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedDate(Instant lastModifiedDate) {

    }
}
