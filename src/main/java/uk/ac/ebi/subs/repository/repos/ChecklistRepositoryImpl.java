package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.subs.repository.model.Checklist;
import uk.ac.ebi.subs.repository.repos.schema.ValidationSchema;

import java.util.List;

@Repository
public class ChecklistRepositoryImpl implements ChecklistRepositoryCustom {

    private MongoTemplate mongoTemplate;

    public ChecklistRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ValidationSchema> findAllValidationSchema() {
        Query query = new Query();
        query.addCriteria(Criteria.where("validationSchema").exists(true));
        query.fields()
                .include("_id")
                .include("dataTypeId")
                .include("displayName")
                .include("description")
                .include("lastModifiedDate")
                .include("validationSchema");

        return mongoTemplate.find(query, ValidationSchema.class, "checklist");
    }

    @Override
    public String findValidationSchemaById(String schemaId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("validationSchema").exists(true))
                .addCriteria(Criteria.where("_id").is(schemaId));

        return mongoTemplate.findOne(query, Checklist.class).getValidationSchema();
    }

}
