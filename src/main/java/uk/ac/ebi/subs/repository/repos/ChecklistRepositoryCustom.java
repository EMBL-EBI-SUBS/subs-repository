package uk.ac.ebi.subs.repository.repos;

import uk.ac.ebi.subs.repository.repos.schema.ValidationSchema;

import java.io.IOException;
import java.util.List;

public interface ChecklistRepositoryCustom {

    List<ValidationSchema> findAllValidationSchema() throws IOException;

    String findValidationSchemaById(String schemaId);
}
