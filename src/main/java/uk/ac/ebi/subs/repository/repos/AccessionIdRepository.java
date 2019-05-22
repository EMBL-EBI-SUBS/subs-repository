package uk.ac.ebi.subs.repository.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.subs.repository.model.accession.AccessionIdWrapper;

import java.util.List;

@Repository
public interface AccessionIdRepository extends MongoRepository<AccessionIdWrapper, String> {

    AccessionIdWrapper findBySubmissionId(String submissionId);
    List<AccessionIdWrapper> findByMessageSentDateIsNotNull();
}
