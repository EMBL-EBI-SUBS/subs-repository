package uk.ac.ebi.subs.repository.repos.fileupload;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.subs.repository.model.fileupload.GlobusShare;

public interface GlobusShareRepository extends MongoRepository<GlobusShare, String> {
}
