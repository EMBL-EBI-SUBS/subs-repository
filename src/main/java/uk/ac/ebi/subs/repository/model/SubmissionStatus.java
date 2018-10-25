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
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import uk.ac.ebi.subs.data.component.Team;
import uk.ac.ebi.subs.data.status.SubmissionStatusEnum;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@CompoundIndexes({
        @CompoundIndex(background = true, name = "team", def = "{ 'team.name': 1}")
})
@Document
public class SubmissionStatus extends uk.ac.ebi.subs.data.status.SubmissionStatus implements Identifiable<String> {

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

    private Team team;

    public SubmissionStatus() {
    }
    public SubmissionStatus(SubmissionStatusEnum submissionStatusEnum) {
        super(submissionStatusEnum);
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

    public Team getTeam() {return team;}

    public void setTeam(Team team) {this.team = team;}
}
