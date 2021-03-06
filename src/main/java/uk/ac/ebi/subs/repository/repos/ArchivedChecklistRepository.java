package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.subs.repository.model.ArchivedChecklist;

@Repository
public interface ArchivedChecklistRepository extends MongoRepository<ArchivedChecklist, String> {
}
