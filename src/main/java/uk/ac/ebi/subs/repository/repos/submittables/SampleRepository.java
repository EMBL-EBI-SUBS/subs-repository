package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.subs.repository.model.Sample;

@RepositoryRestResource
public interface SampleRepository extends SubmittableRepository<Sample> {

    @RestResource(exported = false)
    Sample findByAccession(String accession);

}
