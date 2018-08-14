package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.subs.repository.model.DataTypeTemplate;

@RepositoryRestResource
public interface DataTypeTemplateRepository extends MongoRepository<DataTypeTemplate, String> {

    @Override
    @RestResource(exported = false) // Prevent exposing PUT /things and PATCH /things/:id through REST
    public <S extends DataTypeTemplate> S save(S entity);

    @Override
    @RestResource(exported = false) // Prevent exposing POST /things
    public <S extends DataTypeTemplate> S insert(S s);

    @Override
    @RestResource(exported = false) // Prevent exposing  DELETE /things/:id
    public void delete(DataTypeTemplate entity);
}
