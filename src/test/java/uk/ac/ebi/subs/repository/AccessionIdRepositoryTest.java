package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.subs.repository.model.accession.AccessionIdWrapper;
import uk.ac.ebi.subs.repository.repos.AccessionIdRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class AccessionIdRepositoryTest {

    @Autowired
    private AccessionIdRepository accessionIdRepository;

    private static final List<String> SUBMISSION_IDS = new ArrayList<>();
    private static final int NUMBER_OF_SUBMISSION = 5;

    private static final String BIOSTUDIES_ACCESSIONID = "S-SUBS987654321";
    private static final List<String> BIOSAMPLES_ACCESSIONIDS = new ArrayList<>();
    private static final int NUMBER_OF_BIOSAMPLE_ACCESSIONIDS = 5;

    @Before
    public void setup() {
        generateBiosamplesAccessionIds();

        for (int i = 0; i < NUMBER_OF_SUBMISSION; i++) {
            SUBMISSION_IDS.add(UUID.randomUUID().toString());
            createAccessionIdWrapper(SUBMISSION_IDS.get(i),i % 2 == 0);
        }
    }

    @After
    public void teardown() {
        accessionIdRepository.deleteAll();
    }

    @Test
    public void whenAccessionIdsBySubmisionIDInRepoThenItReturnsThem() {
        AccessionIdWrapper accessionIdWrapper = accessionIdRepository.findBySubmissionId(SUBMISSION_IDS.get(0));

        assertThat(accessionIdWrapper.getSubmissionId(), is(equalTo(SUBMISSION_IDS.get(0))));
    }

    @Test
    public void when5AccessionIdsBySubmissionIDInRepoAnd3AlreadySentThenOnlyReturnsTheNotSent() {
        List<AccessionIdWrapper> accessionIdWrappers = accessionIdRepository.findByMessageSentDateIsNull();

        assertThat(accessionIdWrappers.size(), is(equalTo(2)));
    }

    private void generateBiosamplesAccessionIds() {
        for (int i = 0; i < NUMBER_OF_BIOSAMPLE_ACCESSIONIDS; i++) {
            BIOSAMPLES_ACCESSIONIDS.add(UUID.randomUUID().toString());
        }
    }

    private void createAccessionIdWrapper(String submissionId, boolean hasSent) {
        AccessionIdWrapper accessionIdWrapper = new AccessionIdWrapper();
        accessionIdWrapper.setId(UUID.randomUUID().toString());
        accessionIdWrapper.setSubmissionId(submissionId);
        accessionIdWrapper.setBioStudiesAccessionId(BIOSTUDIES_ACCESSIONID);
        accessionIdWrapper.setBioSamplesAccessionIds(BIOSAMPLES_ACCESSIONIDS);

        if (hasSent) {
            accessionIdWrapper.setMessageSentDate(LocalDateTime.now());
        }

        accessionIdRepository.save(accessionIdWrapper);
    }
}
