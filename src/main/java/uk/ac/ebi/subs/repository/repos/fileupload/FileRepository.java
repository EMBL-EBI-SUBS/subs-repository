package uk.ac.ebi.subs.repository.repos.fileupload;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import uk.ac.ebi.subs.data.fileupload.FileStatus;
import uk.ac.ebi.subs.repository.model.fileupload.File;
import uk.ac.ebi.subs.repository.security.PreAuthorizeSubmissionIdTeamName;

import java.util.List;

/**
 * This is a MongoDB data REST repository for {@link File}s.
 * We restrict to use the POST/PUT/PATCH request through this REST repository.
 * A 3rd party application will persist (upload) the file(s) to a storage disk.
 *
 * Some additional finder methods also defined.
 */
@RepositoryRestResource
public interface FileRepository extends MongoRepository<File, String> {

    // controls PUT /things and PATCH /things/:id
    @Override
    @RestResource(exported = false)
    public <F extends File> F save(@P("entity") F entity);

    // controls POST /things
    @Override
    @RestResource(exported = false)
    public <F extends File> F insert(@P("entity") F s);

    // exported as DELETE /things/:id
    @Override
    @RestResource(exported = false)
    public void delete(@P("entity") File entity);

    @PreAuthorizeSubmissionIdTeamName
    File findByGeneratedTusId(String generatedTusId);

    @RestResource(exported = false)
    List<File> findBySubmissionId(String submissionId);

    @RestResource(path= "by-submission", rel= "by-submission")
    @PreAuthorizeSubmissionIdTeamName
    Page<File> findBySubmissionId(@P("submissionId") @Param("submissionId") String submissionId, Pageable pageable);

    @PreAuthorizeSubmissionIdTeamName
    File findByFilenameAndSubmissionId(String filename, String submissionId);

    @PreAuthorizeSubmissionIdTeamName
    Page<File> findByStatus(FileStatus status, Pageable pageable);
}
