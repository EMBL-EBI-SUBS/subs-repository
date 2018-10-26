package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.Checklist;
import uk.ac.ebi.subs.repository.model.templates.Template;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource
public interface ChecklistRepository extends MongoRepository<Checklist, String>  {

    @RestResource(exported = true, path = "by-data-type-id", rel = "by-data-type-id")
    Page<Checklist> findByDataTypeId(@P("dataTypeId") @Param("dataTypeId") String dataTypeId);

    @Override
    @RestResource(exported = false) // Prevent exposing PUT /things and PATCH /things/:id through REST
    <S extends Checklist> S save(S entity);

    @Override
    @RestResource(exported = false) // Prevent exposing POST /things
    <S extends Checklist> S insert(S s);

    @Override
    @RestResource(exported = false) // Prevent exposing  DELETE /things/:id
    void delete(Checklist entity);

}
