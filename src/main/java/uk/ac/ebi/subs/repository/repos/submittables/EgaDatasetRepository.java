package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.subs.repository.model.EgaDataset;
import uk.ac.ebi.subs.repository.projections.SubmittableWithStatus;

@RepositoryRestResource(excerptProjection = SubmittableWithStatus.class)
public interface EgaDatasetRepository extends SubmittableRepository<EgaDataset> {

}
