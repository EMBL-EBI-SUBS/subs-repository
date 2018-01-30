package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.templates.Template;
import uk.ac.ebi.subs.repository.security.PreAuthorizeAdminTeam;

@RepositoryRestResource
public interface TemplateRepository extends MongoRepository<Template, String> {

    // exported as GET /Templates/:id
    @Override
    @RestResource(exported = true)
    Template findOne(String id);

    // exported as GET /Templates
    @Override
    @RestResource(exported = true)
    Page<Template> findAll(Pageable pageable);

    @RestResource(exported = true, rel = "by-name")
    Template findOneByName(@Param("name") String name);

    @RestResource(exported = true, rel = "by-target-type")
    Page<Template> findByTargetType(@Param("name") String targetType,Pageable pageable);

    // Restricts PUT /Templates/:id and PATCH /Templates/:id to admin users
    @Override
    @RestResource(exported = true)
    @PreAuthorizeAdminTeam
    <S extends Template> S save(@P("entity") S entity);

    // Restricts POST /Templates/:id to admin users
    @Override
    @RestResource(exported = true)
    @PreAuthorizeAdminTeam
    public <S extends Template> S insert(@P("entity") S entity);

    // Restricts as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeAdminTeam
    void delete(@P("entity") Template entity);

}