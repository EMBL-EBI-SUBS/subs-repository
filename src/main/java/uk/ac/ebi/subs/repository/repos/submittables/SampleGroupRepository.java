package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.subs.repository.model.SampleGroup;
import uk.ac.ebi.subs.repository.projections.SubmittableWithStatus;

@RepositoryRestResource
public interface SampleGroupRepository extends SubmittableRepository<SampleGroup> {

}
