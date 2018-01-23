package uk.ac.ebi.subs.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.data.component.Team;
import uk.ac.ebi.subs.repository.model.ProcessingStatus;
import uk.ac.ebi.subs.repository.model.SubmissionStatus;
import uk.ac.ebi.subs.repository.repos.status.ProcessingStatusRepository;
import uk.ac.ebi.subs.repository.repos.status.SubmissionStatusRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SubmissionStatusRepositoryTest {

    @Autowired
    private SubmissionStatusRepository submissionStatusRepository;

    @Before
    public void buildUp() {
        submissionStatusRepository.deleteAll();
    }

    @Test
    public void testAggregationWithNoData() {
        Map<String,Integer> statusCounts = submissionStatusRepository.submissionStatusCountsByTeam(Arrays.asList("foo"));
        assertThat(statusCounts,notNullValue());
        assertThat(statusCounts,is(equalTo(new HashMap<>())));
    }

    @Test
    public void testAggregation() {
        Map<String,Integer> expected = new HashMap<>();
        expected.put("bad",1);
        expected.put("ok",2);

        for (Map.Entry<String,Integer> entry : expected.entrySet()){
            for (int i = 0; i < entry.getValue() ; i++){
                submissionStatusRepository.insert(status(entry.getKey(),"foo"));
            }
        }

        Map<String,Integer> statusCounts = submissionStatusRepository.submissionStatusCountsByTeam(Arrays.asList("foo"));
        assertThat(statusCounts,notNullValue());

        assertThat(statusCounts,is(equalTo(expected)));
    }

    private SubmissionStatus status(String statusName, String team) {
        SubmissionStatus status = new SubmissionStatus();

        status.setStatus(statusName);
        status.setTeam(Team.build(team));

        return status;
    }

}
