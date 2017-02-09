package uk.ac.ebi.subs.repository.repos;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;


public interface SubmittableRepositoryCustom<T extends StoredSubmittable> {

    Page<T> itemsByDomain(String domainName, Pageable pageable);
}
