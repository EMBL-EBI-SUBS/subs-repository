package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.subs.data.SubmissionLinks;
import uk.ac.ebi.subs.data.submittable.Protocol;

public interface ProtocolRepository extends SubmittableRepository<Protocol, String>{

    @RestResource(rel= SubmissionLinks.PROTOCOL)
    Page<Protocol> findBySubmissionId(@Param("submissionId") String submissionId, Pageable pageable);

}
