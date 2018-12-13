package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.subs.repository.model.EgaDacPolicy;

@RepositoryRestResource
public interface EgaDacPolicyRepository extends SubmittableRepository<EgaDacPolicy> {


}
