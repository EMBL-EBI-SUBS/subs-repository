package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.sheets.Spreadsheet;
import uk.ac.ebi.subs.repository.projections.SheetBasicsProjection;
import uk.ac.ebi.subs.repository.security.PostAuthorizeReturnObjectHasTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmissionIdTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmittableTeamName;

import java.util.Date;
import java.util.Optional;

/**
 * Created by Dave on 21/10/2017.
 */
@RepositoryRestResource(excerptProjection = SheetBasicsProjection.class)
public interface SpreadsheetRepository extends MongoRepository<Spreadsheet,String> {

    // exported as GET /things/:id
    @Override
    @RestResource(exported = true)
    @PostAuthorizeReturnObjectHasTeamName
    public Optional<Spreadsheet> findById(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = false)
    public Page<Spreadsheet> findAll(Pageable pageable);

    // controls PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public <S extends Spreadsheet> S save(@P("entity") S entity);

    // controls POST /things
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public <S extends Spreadsheet> S insert(@P("entity") S s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public void delete(@P("entity") Spreadsheet entity);


    @RestResource(exported = true, path = "by-submission", rel = "by-submission")
    @PreAuthorizeSubmissionIdTeamName
    Page<Spreadsheet> findBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId, Pageable pageable);

    @RestResource(exported = true, path= "by-submission-and-data-type",rel= "by-submission-and-data-type")
    @PreAuthorizeSubmissionIdTeamName
    Page<Spreadsheet> findBySubmissionIdAndDataTypeIdOrderByCreatedDateDesc(
            @P("submissionId") @Param("submissionId") String submissionId,
            @P("dataTypeId") @Param("dataTypeId") String dataTypeId,
            Pageable pageable
    );

    @RestResource(exported = false)
    void removeByLastModifiedDateBeforeAndStatus(Date lastModifiedBy, String status);

    @RestResource(exported = false)
    void deleteBySubmissionId(String submissionId);

}
