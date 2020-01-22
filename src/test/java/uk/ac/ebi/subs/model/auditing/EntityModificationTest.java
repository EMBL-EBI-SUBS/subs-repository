package uk.ac.ebi.subs.model.auditing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.subs.data.component.Submitter;
import uk.ac.ebi.subs.repository.config.Config;
import uk.ac.ebi.subs.repository.model.*;
import uk.ac.ebi.subs.repository.repos.SubmissionRepository;
import uk.ac.ebi.subs.repository.repos.submittables.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableMongoAuditing
public class EntityModificationTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private List<SubmittableRepository> submittableRepositories;

    @Test
    public void testSubmissionLastModifiedDateChange() {
        Submitter submitter = new Submitter();
        submitter.setName("submitter-1");

        Submission submission = new Submission();
        submission.setId(UUID.randomUUID().toString());
        submission.setName("submission-1");
        submission.setSubmitter(submitter);

        submissionRepository.save(submission);

        submission = submissionRepository.findOne(submission.getId());
        Date beforeModification = submission.getLastModifiedDate();

        // change object.
        submission.setName("submission-2");
        submissionRepository.save(submission);
        submission = submissionRepository.findOne(submission.getId());
        Date afterModification = submission.getLastModifiedDate();

        assertNotEquals(beforeModification, afterModification);

        beforeModification = afterModification;

        // change nested object.
        submission.getSubmitter().setName("submitter-2");
        submissionRepository.save(submission);
        submission = submissionRepository.findOne(submission.getId());
        afterModification = submission.getLastModifiedDate();

        assertTrue(afterModification.after(beforeModification));

        submissionRepository.deleteAll();
    }

    @Test
    public void testStoredSubmittableLastModifiedDateChange() {

        List<Pair<StoredSubmittable, Class<? extends SubmittableRepository>>> storedSubmittableRepoPair = Arrays.asList(
                Pair.of(new Analysis(), AnalysisRepository.class), Pair.of(new Assay(), AssayRepository.class),
                Pair.of(new AssayData(), AssayDataRepository.class), Pair.of(new EgaDac(), EgaDacRepository.class),
                Pair.of(new EgaDacPolicy(), EgaDacPolicyRepository.class),
                Pair.of(new EgaDataset(), EgaDatasetRepository.class),
                Pair.of(new Project(), ProjectRepository.class), Pair.of(new Protocol(), ProtocolRepository.class),
                Pair.of(new Sample(), SampleRepository.class), Pair.of(new SampleGroup(), SampleGroupRepository.class),
                Pair.of(new Study(), StudyRepository.class)
        );

        storedSubmittableRepoPair.forEach(pair -> {
            SubmittableRepository submittableRepository = submittableRepositories.stream()
                    .filter(repo -> pair.getSecond().isAssignableFrom(repo.getClass())).findFirst().get();

            StoredSubmittable ss = pair.getFirst();
            ss.setId(UUID.randomUUID().toString());
            ss.setDescription("description-1");

            submittableRepository.save(ss);

            Date beforeModification = ss.getLastModifiedDate();

            ss.setDescription("description-2");

            submittableRepository.save(ss);

            ss = (StoredSubmittable) submittableRepository.findOne(ss.getId());

            Date afterModification = ss.getLastModifiedDate();

            assertTrue(afterModification.after(beforeModification));

            submittableRepository.deleteAll();
        });
    }
}
