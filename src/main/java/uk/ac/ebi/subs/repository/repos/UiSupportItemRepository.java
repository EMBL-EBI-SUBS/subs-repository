package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.UiSupportItem;
import uk.ac.ebi.subs.repository.security.PreAuthorizeAdminTeam;


/**
 * UI support items can be created/edited/deleted by admins and read by anyone
 */
public interface UiSupportItemRepository extends MongoRepository<UiSupportItem, String> {

    // exported as GET /uiSupportItems/:id
    @Override
    @RestResource(exported = true)
    UiSupportItem findOne(String id);

    // exported as GET /uiSupportItems
    @Override
    @RestResource(exported = true)
    Page<UiSupportItem> findAll(Pageable pageable);

    @RestResource(exported = true,rel="by-name")
    UiSupportItem findOneByName(String name);

    // Restricts PUT /uiSupportItems/:id and PATCH /uiSupportItems/:id to admin users
    @Override
    @RestResource(exported = true)
    @PreAuthorizeAdminTeam
    <S extends UiSupportItem> S save(@P("entity") S entity);

    // Restricts POST /uiSupportItems/:id to admin users
    @Override
    @RestResource(exported = true)
    @PreAuthorizeAdminTeam
    public <S extends UiSupportItem> S insert(@P("entity") S entity);

    // Restricts as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeAdminTeam
    void delete(@P("entity") UiSupportItem entity);

}
