package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.subs.data.component.Archive;
import uk.ac.ebi.subs.repository.model.DataType;

import java.util.List;

@RepositoryRestResource
public interface DataTypeRepository extends MongoRepository<DataType, String> {

    @Override
    @RestResource(exported = false) // Prevent exposing PUT /things and PATCH /things/:id through REST
    public <S extends DataType> S save(S entity);

    @Override
    @RestResource(exported = false) // Prevent exposing POST /things
    public <S extends DataType> S insert(S s);

    @Override
    @RestResource(exported = false) // Prevent exposing  DELETE /things/:id
    public void delete(DataType entity);

    List<DataType> findByArchive(Archive archive);
}
