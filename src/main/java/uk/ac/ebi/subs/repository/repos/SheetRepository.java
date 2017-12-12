package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.Submission;
import uk.ac.ebi.subs.repository.model.sheets.Sheet;
import uk.ac.ebi.subs.repository.projections.SheetBasicsProjection;
import uk.ac.ebi.subs.repository.projections.SubmissionWithStatus;
import uk.ac.ebi.subs.repository.security.PostAuthorizeReturnObjectHasTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeParamTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmissionIdTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmissionTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmittableTeamName;

import java.util.List;

/**
 * Created by Dave on 21/10/2017.
 */
@RepositoryRestResource(excerptProjection = SheetBasicsProjection.class)
public interface SheetRepository extends MongoRepository<Sheet,String> {

    // exported as GET /things/:id
    @Override
    @RestResource(exported = true)
    @PostAuthorizeReturnObjectHasTeamName
    public Sheet findOne(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = false)
    public Page<Sheet> findAll(Pageable pageable);

    // controls PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public <S extends Sheet> S save(@P("entity") S entity);

    // controls POST /things
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public <S extends Sheet> S insert(@P("entity") S s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public void delete(@P("entity") Sheet entity);


    @RestResource(exported = true, path = "by-submission", rel = "by-submission")
    @PreAuthorizeSubmissionIdTeamName
    Page<Sheet> findBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId, Pageable pageable);

    @RestResource(exported = true, path= "by-submission-and-target-type",rel= "by-submission-and-target-type")
    @PreAuthorizeSubmissionIdTeamName
    Page<Sheet> findBySubmissionIdAndTemplateTargetType(
            @P("submissionId") @Param("submissionId") String submissionId,
            @P("templateTargetType") @Param("templateTargetType") String templateTargetType,
            Pageable pageable
    );



    @RestResource(exported = false)
    void deleteBySubmissionId(String submissionId);

}
