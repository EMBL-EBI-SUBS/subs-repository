package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.subs.data.core.Submittable;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
@RepositoryRestResource
public interface SubmittableRepository<T extends Submittable,ID extends Serializable> extends MongoRepository<T, ID> {

    @RestResource(exported = false)
    List<T> findBySubmissionId(ID submissionId);
}
