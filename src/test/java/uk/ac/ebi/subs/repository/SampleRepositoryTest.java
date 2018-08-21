package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.repository.model.DataType;
import uk.ac.ebi.subs.repository.model.Sample;
import uk.ac.ebi.subs.repository.model.Submission;
import uk.ac.ebi.subs.repository.repos.DataTypeRepository;
import uk.ac.ebi.subs.repository.repos.SubmissionRepository;
import uk.ac.ebi.subs.repository.repos.submittables.SampleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SampleRepositoryTest {

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    DataTypeRepository dataTypeRepository;

    @Autowired
    SampleRepository sampleRepository;

    Submission testSub;

    DataType testDataType;

    List<Sample> samples;

    PageRequest pageRequest = new PageRequest(0, 10);

    @Before
    public void buildUp() {
        tearDown();
        
        testDataType = new DataType();
        testDataType.setId("samples");
        dataTypeRepository.insert(testDataType);
    }

    private void submissionWithTwoSamples() {
        samples = new ArrayList<>();

        testSub = new Submission();
        testSub.getSubmitter().setEmail("test@example.ac.uk");
        testSub.getTeam().setName("testTeam");
        testSub.setId(UUID.randomUUID().toString());
        submissionRepository.insert(testSub);



        samples.add(new Sample());
        samples.add(new Sample());

        samples.get(0).setAlias("one");
        samples.get(1).setAlias("two");

        for (Sample s : samples){
            s.setId(UUID.randomUUID().toString());
            s.setTeam(testSub.getTeam());
            s.setCreatedDate(new Date());
            s.setDataType(testDataType);
            s.setSubmission(testSub);
        }


        sampleRepository.insert(samples);

    }

    @After
    public void tearDown() {
        submissionRepository.deleteAll();
        sampleRepository.deleteAll();
        dataTypeRepository.deleteAll();
    }

    @Test
    public void testOneSubmission() {

        submissionWithTwoSamples();

        assertThat(sampleRepository.findBySubmissionId(testSub.getId(), pageRequest).getTotalElements(), is(equalTo((long) samples.size())));

        assertThat(sampleRepository.submittablesInTeam(testSub.getTeam().getName(), pageRequest).getTotalElements(), is(equalTo((long) samples.size())));

        assertThat(sampleRepository.findFirstByTeamNameAndAliasOrderByCreatedDateDesc(testSub.getTeam().getName(), "two"), notNullValue());


        assertThat(sampleRepository.findBySubmissionIdAndAliasIn(testSub.getId(), Arrays.asList("two")).size(), is(equalTo(1)));

        assertThat(sampleRepository.findBySubmissionIdAndDataTypeId(testSub.getId(),testDataType.getId(),pageRequest).getTotalElements(),is(equalTo((long) samples.size())));
    }

    @Test
    public void testTwoSubmissions() {
        submissionWithTwoSamples();
        submissionWithTwoSamples();

        Page<Sample> samplesInTeam = sampleRepository.submittablesInTeam(testSub.getTeam().getName(), pageRequest);

        assertThat(samplesInTeam.getContent(), hasSize(2));

        Page<Sample> sampleHistory = sampleRepository.findByTeamNameAndAliasOrderByCreatedDateDesc(
                testSub.getTeam().getName(),
                samples.get(1).getAlias(),
                pageRequest
        );

        assertThat(sampleHistory.getContent(), hasSize(2));
        sampleHistory.getContent().forEach(s -> assertThat(s.getAlias(), is(equalTo(samples.get(1).getAlias()))));
        Sample topEntry = sampleHistory.getContent().get(0);
        //should be most recent version
        assertThat(topEntry.getCreatedDate(), is(equalTo(samples.get(1).getCreatedDate())));

        Sample currentVersion = sampleRepository.findFirstByTeamNameAndAliasOrderByCreatedDateDesc(testSub.getTeam().getName(), samples.get(1).getAlias());
        //should be most recent version
        assertThat(currentVersion.getCreatedDate(), is(equalTo(samples.get(1).getCreatedDate())));

        PageRequest sortedPageRequest = new PageRequest(0, 10, Sort.Direction.DESC,"alias");

        Page<Sample> aliaseSortedSamplesInTeam = sampleRepository.submittablesInTeam(
                testSub.getTeam().getName(),sortedPageRequest
        );

    }


}
