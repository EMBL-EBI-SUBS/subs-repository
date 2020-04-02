package uk.ac.ebi.subs.repository.repos.schema;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class ValidationSchema {

    @Field("_id")
    private String id;

    private String dataTypeId;
    private String displayName;
    private String description;
    private Date lastModifiedDate;

    private String validationSchema;
}
