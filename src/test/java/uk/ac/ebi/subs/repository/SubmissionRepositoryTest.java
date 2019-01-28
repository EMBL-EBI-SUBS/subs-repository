package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.repository.model.Submission;
import uk.ac.ebi.subs.repository.repos.SubmissionRepository;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SubmissionRepositoryTest {

    @Autowired
    SubmissionRepository submissionRepository;

    Submission testSub;

    @Before
    public void buildUp() {
        testSub = new Submission();
        testSub.getSubmitter().setEmail("test@example.ac.uk");
        testSub.getTeam().setName("testTeam" + Math.random());
        testSub.setId(UUID.randomUUID().toString());
    }

    @After
    public void tearDown() {
        submissionRepository.delete(testSub.getId());
    }

    @Test
    public void storeSubmission() {
        submissionRepository.save(testSub);

        assertSubmissionStored();
    }

    @Test
    public void listSubmissions() {
        storeSubmission();
        Pageable pageable = new PageRequest(0, 10);
        Page<Submission> subs = submissionRepository.findByTeamNameInOrderByCreatedByDesc(
                Arrays.asList(testSub.getTeam().getName()),
                pageable
        );

        assertThat(subs.getTotalElements(),equalTo(1L));

    }

    private void assertSubmissionStored() {
        Submission stored = submissionRepository.findOne(testSub.getId());
        assertThat("Submission stored", stored.getTeam().getName(), equalTo(testSub.getTeam().getName()));
    }

}
