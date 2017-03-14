package uk.ac.ebi.subs.repository.repos.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.subs.repository.model.ProcessingStatus;
import uk.ac.ebi.subs.repository.projections.ProcessingStatusWithAlias;

import java.util.List;


@RepositoryRestResource(excerptProjection = ProcessingStatusWithAlias.class)
public interface ProcessingStatusRepository extends MongoRepository<ProcessingStatus, String>, ProcessingStatusRepositoryCustom {

    // exported as GET /things/:id
    @Override
    @RestResource(exported = true)
    public ProcessingStatus findOne(String id);

    // exported as GET /things
    @Override
    @RestResource(exported = false)
    Page<ProcessingStatus> findAll(Pageable pageable);

    // Prevents POST /things and PATCH /things/:id
    @Override
    @RestResource(exported = true)
    <S extends ProcessingStatus> S save(S s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = false)
    void delete(ProcessingStatus t);

    @RestResource(exported = false)
    List<ProcessingStatus> findBySubmissionId(String submissionId);

    @RestResource(exported = true)
    ProcessingStatus findBySubmittableId(@Param("itemId") String itemId);

    @RestResource(exported = false)
    void deleteBySubmissionId(String submissionId);

    @RestResource(exported = true, rel = "by-submission")
    Page<ProcessingStatus> findBySubmissionId(@Param("submissionId") String submissionId, Pageable pageable);

    @RestResource(exported = true, rel = "by-submission-and-type")
    Page<ProcessingStatus> findBySubmissionIdAndSubmittableType(@Param("submissionId") String submissionId, @Param("type") String type, Pageable pageable);
}
