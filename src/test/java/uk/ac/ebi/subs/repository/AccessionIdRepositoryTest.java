package uk.ac.ebi.subs.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.subs.repository.model.accession.AccessionIdWrapper;
import uk.ac.ebi.subs.repository.repos.AccessionIdRepository;

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

    private static final String SUBMISSION_ID = UUID.randomUUID().toString();
    private static final String BIOSTUDIES_ACCESSIONID = "S-SUBS987654321";
    private static final List<String> BIOSAMPLES_ACCESSIONIDS = new ArrayList<>();
    private static final int NUMBER_OF_BIOSAMPLE_ACCESSIONIDS = 5;

    @Before
    public void setup() {
        generateBiosamplesAccessionIds();
        createAccessionIdWrapper();
    }

    @Test
    public void whenAccessionIdsBySubmisionIDInRepoThenItReturnsThem() {
        AccessionIdWrapper accessionIdWrapper = accessionIdRepository.findBySubmissionId(SUBMISSION_ID);

        assertThat(accessionIdWrapper.getSubmissionId(), is(equalTo(SUBMISSION_ID)));
    }

    private void generateBiosamplesAccessionIds() {
        for (int i = 0; i < NUMBER_OF_BIOSAMPLE_ACCESSIONIDS; i++) {
            BIOSAMPLES_ACCESSIONIDS.add(UUID.randomUUID().toString());
        }
    }

    private void createAccessionIdWrapper() {
        AccessionIdWrapper accessionIdWrapper = new AccessionIdWrapper();
        accessionIdWrapper.setId(UUID.randomUUID().toString());
        accessionIdWrapper.setSubmissionId(SUBMISSION_ID);
        accessionIdWrapper.setBioStudiesAccessionId(BIOSTUDIES_ACCESSIONID);
        accessionIdWrapper.setBioSamplesAccessionIds(BIOSAMPLES_ACCESSIONIDS);

        accessionIdRepository.save(accessionIdWrapper);
    }
}
