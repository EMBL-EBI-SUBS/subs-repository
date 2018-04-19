package uk.ac.ebi.subs.repository.model.fileupload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This MongoDB document describes information of a file to be uploaded to the various biological archives.
 */
@Document
@Data
@EqualsAndHashCode(callSuper=true)
@CompoundIndexes({
        @CompoundIndex(background = true, name = "generatedTusId", def = "{ 'generatedTusId': 1 }"),
        @CompoundIndex(background = true, name = "filename", def = "{ 'filename': 1 }"),
        @CompoundIndex(background = true, name = "submissionId", def = "{ 'submissionId': 1 }")
})
public class File extends uk.ac.ebi.subs.data.fileupload.File {

    @Id
    private String id;
           }