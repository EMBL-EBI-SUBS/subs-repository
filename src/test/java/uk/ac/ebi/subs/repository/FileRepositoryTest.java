package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.subs.repository.model.fileupload.File;
import uk.ac.ebi.subs.repository.model.fileupload.FileStatus;
import uk.ac.ebi.subs.repository.repos.fileupload.FileRepository;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    private List<File> files;
    private static final String FILENAME_1 = "test_file.cram";
    private static final String FILENAME_2 = "test_file.bam";
    private static final String FILENAME_3 = "test_file2.bam";
    private static final String SUBMISSION_ID_1 = "submissionID_12345";
    private static final String SUBMISSION_ID_2 = "submissionID_67890";
    private static final String SUBMISSION_ID_3 = "submissionID_11223";
    private static final String USER_1 = "Bela";
    private static final String USER_2 = "Lajos";
    private static final long TOTAL_SIZE = 12345678L;
    private static final long UPLOADED_SIZE = 12345678L;
    private static final String TUS_ID_1 = "TUSID_12";
    private static final String TUS_ID_2 = "TUSID_34";
    private static final String TUS_ID_3 = "TUSID_56";
    private static final String TUS_ID_4 = "TUSID_78";

    private PageRequest pageRequest = new PageRequest(0, 10);

    @Before
    public void setup() {
        cleanupRepository();
        files = new ArrayList<>();
    }

    @After
    public void tearDown() {
        cleanupRepository();
        files.clear();
    }

    @Test
    public void testFindFileByTusId() {
        createAndPersistFileIntoRepo(TUS_ID_1, FILENAME_1, SUBMISSION_ID_1, USER_1, FileStatus.INITIALIZED);

        File persistedFile = fileRepository.findByGeneratedTusId(TUS_ID_1);

        assertEquals(persistedFile, files.get(0));
    }

    @Test
    public void testFindFileBySubmissionId() {
        createAndPersistFileIntoRepo(TUS_ID_1, FILENAME_1, SUBMISSION_ID_1, USER_1, FileStatus.UPLOADING);

        Page<File> persistedFiles = fileRepository.findBySubmissionId(SUBMISSION_ID_1, pageRequest);

        assertEquals(persistedFiles.getContent().get(0), files.get(0));
    }

    @Test
    public void testFindFileByFilenameAndSubmissionId() {
        createAndPersistFileIntoRepo(TUS_ID_1, FILENAME_1, SUBMISSION_ID_1, USER_1, FileStatus.UPLOADED);
        createAndPersistFileIntoRepo(TUS_ID_2, FILENAME_1, SUBMISSION_ID_2, USER_1, FileStatus.UPLOADED);
        createAndPersistFileIntoRepo(TUS_ID_3, FILENAME_2, SUBMISSION_ID_1, USER_1, FileStatus.UPLOADING);

        File persistedFile = fileRepository.findByFilenameAndSubmissionId(FILENAME_1, SUBMISSION_ID_1);

        assertThat(fileRepository.count(), is((long)(files.size())));
        assertEquals(persistedFile, files.get(0));
    }

    @Test
    public void testFindFileByStatus() {
        createAndPersistFileIntoRepo(TUS_ID_1, FILENAME_1, SUBMISSION_ID_1, USER_1, FileStatus.UPLOADED);
        createAndPersistFileIntoRepo(TUS_ID_2, FILENAME_1, SUBMISSION_ID_2, USER_1, FileStatus.INITIALIZED);
        createAndPersistFileIntoRepo(TUS_ID_4, FILENAME_3, SUBMISSION_ID_3, USER_2, FileStatus.UPLOADING);
        createAndPersistFileIntoRepo(TUS_ID_3, FILENAME_2, SUBMISSION_ID_1, USER_1, FileStatus.UPLOADED);

        Page<File> initializedFiles = fileRepository.findByStatus(FileStatus.INITIALIZED, pageRequest);
        Page<File> uploadingFiles = fileRepository.findByStatus(FileStatus.UPLOADING, pageRequest);
        Page<File> uploadedFiles = fileRepository.findByStatus(FileStatus.UPLOADED, pageRequest);

        assertThat(fileRepository.count(), is((long)(files.size())));

        assertThat(initializedFiles.getTotalElements(), is(1L));
        assertThat(uploadingFiles.getTotalElements(), is(1L));
        assertThat(uploadedFiles.getTotalElements(), is(2L));

        assertEquals(initializedFiles.getContent().get(0), files.get(1));
        assertEquals(uploadingFiles.getContent().get(0), files.get(2));
        assertEquals(uploadedFiles.getContent().get(0), files.get(0));
        assertEquals(uploadedFiles.getContent().get(1), files.get(3));
    }

    private void cleanupRepository() {
        fileRepository.deleteAll();;
    }

    private void createAndPersistFileIntoRepo(
            String tusId, String filename, String submissionId, String user, FileStatus status) {
        File file = new File();
        file.setTotalSize(TOTAL_SIZE);
        file.setUploadedSize(UPLOADED_SIZE);
        file.setGeneratedTusId(tusId);
        file.setFilename(filename);
        file.setSubmissionId(submissionId);
        file.setCreatedBy(user);
        file.setStatus(status);

        fileRepository.insert(file);
        files.add(file);
    }
}
