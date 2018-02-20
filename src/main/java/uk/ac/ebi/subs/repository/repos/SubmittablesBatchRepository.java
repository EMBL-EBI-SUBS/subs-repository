package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.SubmittablesBatch;
import uk.ac.ebi.subs.repository.security.PostAuthorizeReturnObjectHasTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmissionIdTeamName;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmittableTeamName;

import java.util.Date;

@RepositoryRestResource
public interface SubmittablesBatchRepository extends MongoRepository<SubmittablesBatch, String> {

    // exported as GET /things/:id
    @Override
    @RestResource(exported = true)
    @PostAuthorizeReturnObjectHasTeamName
    public SubmittablesBatch findOne(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = false)
    public Page<SubmittablesBatch> findAll(Pageable pageable);

    // controls PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public <S extends SubmittablesBatch> S save(@P("entity") S entity);

    // controls POST /things
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public <S extends SubmittablesBatch> S insert(@P("entity") S s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = true)
    @PreAuthorizeSubmittableTeamName
    public void delete(@P("entity") SubmittablesBatch entity);


    @RestResource(exported = true, path = "by-submission", rel = "by-submission")
    @PreAuthorizeSubmissionIdTeamName
    Page<SubmittablesBatch> findBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId, Pageable pageable);

    @RestResource(exported = true, path = "by-submission-and-target-type", rel = "by-submission-and-target-type")
    @PreAuthorizeSubmissionIdTeamName
    Page<SubmittablesBatch> findBySubmissionIdAndTargetType(
            @P("submissionId") @Param("submissionId") String submissionId,
            @P("targetType") @Param("targetType") String targetType,
            Pageable pageable
    );


    @RestResource(exported = false)
    void removeByLastModifiedDateBeforeAndStatus(Date lastModifiedBy, String status);

}
