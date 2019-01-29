package uk.ac.ebi.subs.repository;

import org.hamcrest.Matchers;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SubmissionRepositoryTest {

    @Autowired
    SubmissionRepository submissionRepository;

    Submission testSub;

    private static final String TEST_TEAM_NAME = "testTeam";
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2019, 01, 12, 11, 11);
    private static final Date CREATED_DATE1 = Date.from(LOCAL_DATE_TIME.atZone(ZoneId.systemDefault()).toInstant());
    private static final Date CREATED_DATE2 = Date.from(LOCAL_DATE_TIME.plusDays(2).atZone(ZoneId.systemDefault()).toInstant());
    private static final Date CREATED_DATE3 = Date.from(LOCAL_DATE_TIME.plusDays(4).atZone(ZoneId.systemDefault()).toInstant());

    @Before
    public void buildUp() {
        testSub = generateSubmission();
    }

    @After
    public void tearDown() {
        submissionRepository.deleteAll();
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
        Page<Submission> subs = submissionRepository.findByTeamNameInOrderByCreatedDateDesc(
                Collections.singletonList(TEST_TEAM_NAME),
                pageable
        );

        assertThat(subs.getTotalElements(),equalTo(1L));

    }

    @Test
    public void testFindByTeamNameInOrderByCreatedByDesc() {
        testSub.setCreatedDate(CREATED_DATE1);
        submissionRepository.save(testSub);

        Submission testSub2 = generateSubmission();
        testSub2.setCreatedDate(CREATED_DATE2);
        submissionRepository.save(testSub2);

        Submission testSub3 = generateSubmission();
        testSub3.setCreatedDate(CREATED_DATE3);
        submissionRepository.save(testSub3);

        Pageable pageable = new PageRequest(0, 10);
        Page<Submission> subs = submissionRepository.findByTeamNameInOrderByCreatedDateDesc(
                Arrays.asList(testSub.getTeam().getName()),
                pageable
        );

        assertThat(subs.getTotalElements(),equalTo(3L));

        List<Submission> retrievedSubmissions = subs.getContent();
        assertThat(retrievedSubmissions.get(0).getCreatedDate(), Matchers.is(equalTo(CREATED_DATE3)));
        assertThat(retrievedSubmissions.get(1).getCreatedDate(), Matchers.is(equalTo(CREATED_DATE2)));
        assertThat(retrievedSubmissions.get(2).getCreatedDate(), Matchers.is(equalTo(CREATED_DATE1)));
    }

    private Submission generateSubmission() {
        Submission submission = new Submission();
        submission.getSubmitter().setEmail("test@example.ac.uk");
        submission.getTeam().setName(TEST_TEAM_NAME);
        submission.setId(UUID.randomUUID().toString());

        return submission;
    }

    private void assertSubmissionStored() {
        Submission stored = submissionRepository.findOne(testSub.getId());
        assertThat("Submission stored", stored.getTeam().getName(), equalTo(TEST_TEAM_NAME));
    }
}
