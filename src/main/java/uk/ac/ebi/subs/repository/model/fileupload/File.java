package uk.ac.ebi.subs.repository.model.fileupload;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This MongoDB document describes information of a file to be uploaded to the various biological archives.
 */
@Document
@Data
public class File {

    @Id
    private String id;

    @Indexed
    private String generatedTusId;

    @Indexed
    private String filename;

    private String uploadPath;

    private String targetPath;

    @Indexed
    private String submissionId;

    private long totalSize;

    private long uploadedSize;

    private String createdBy;

    private FileStatus status;
}