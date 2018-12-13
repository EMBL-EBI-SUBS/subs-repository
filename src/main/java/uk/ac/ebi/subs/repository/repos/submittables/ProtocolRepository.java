package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.subs.repository.model.Protocol;

@RepositoryRestResource
public interface ProtocolRepository extends SubmittableRepository<Protocol> {

}
