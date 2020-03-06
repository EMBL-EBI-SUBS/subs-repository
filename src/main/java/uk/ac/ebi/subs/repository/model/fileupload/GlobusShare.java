package uk.ac.ebi.subs.repository.model.fileupload;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class GlobusShare implements Persistable<String> {

    @Id
    private String owner;

    private String sharedEndpointId;
    private String shareLink;

    private List<String> registeredSubmissionIds = new ArrayList<>();

    @CreatedDate
    private Date createdDate;

    @Override
    public String getId() {
        return this.owner;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
