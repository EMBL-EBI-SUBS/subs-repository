package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.repository.model.Project;
import uk.ac.ebi.subs.repository.projections.SubmittableWithStatus;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmissionIdTeamName;

import java.util.List;

@RepositoryRestResource(excerptProjection = SubmittableWithStatus.class)
public interface ProjectRepository extends SubmittableRepository<Project> {

    @RestResource(exported = true, path = "project-by-submission", rel = "project-by-submission")
    @PreAuthorizeSubmissionIdTeamName
    Project findOneBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId);
}
