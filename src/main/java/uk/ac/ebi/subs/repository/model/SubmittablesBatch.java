package uk.ac.ebi.subs.repository.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import uk.ac.ebi.subs.data.component.Submitter;
import uk.ac.ebi.subs.data.component.Team;
import uk.ac.ebi.subs.repository.model.templates.Template;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document
@Data
public class SubmittablesBatch {

    @Id
    private String id;

    @DBRef
    private Submission submission;

    private String name;
    private String targetType;
    private String status;

    public int getDocumentCount(){
        if (documents == null) return 0;
        return documents.size();
    }
    public int getProcessedDocumentCount(){
        if (documents == null) return 0;
        return (int)documents.stream().filter(SubmittablesBatch.Document::isProcessed).count();
    }


    private List<Document> documents = new LinkedList<>();

    public void addDocument(Document document){
        documents.add(document);
    }

    private Submitter submitter;
    private Team team;

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

    @Data
    public static class Document {
        @JsonRawValue
        private String document;

        private boolean processed = false;

        private List<String> errors = new LinkedList<>();

        public void addError(String error){
            errors.add(error);
        }


        public void setDocument(JsonNode jsonNode) {
            this.document = jsonNode.toString();
        }
    }
}
