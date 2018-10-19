package uk.ac.ebi.subs.model;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.data.component.SampleRef;
import uk.ac.ebi.subs.data.component.StudyRef;
import uk.ac.ebi.subs.repository.model.Analysis;
import uk.ac.ebi.subs.repository.model.Submission;
import uk.ac.ebi.subs.repository.repos.SubmissionRepository;
import uk.ac.ebi.subs.repository.repos.submittables.AnalysisRepository;
import uk.ac.ebi.subs.repository.services.SubmittableHelperService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class StoredSubmittableReferencesTest {


    @Autowired
    AnalysisRepository repository;

    @Autowired
    SubmissionRepository submissionRepository;

    Analysis storedSubmittable;

    StudyRef studyRef;

    SampleRef sampleRef1;
    SampleRef sampleRef2;

    Submission sub;

    @Before
    public void buildUp() {
        studyRef = new StudyRef();
        studyRef.setAccession("foo");

        sampleRef1 = new SampleRef();
        sampleRef1.setAlias("bar");
        sampleRef1.setTeam("wine");

        sampleRef2 = new SampleRef();
        sampleRef2.setAlias("biscuit");
        sampleRef2.setTeam("tea");

        storedSubmittable = new Analysis();

        storedSubmittable.setStudyRefs(Arrays.asList(studyRef));
        storedSubmittable.setSampleRefs(Arrays.asList(sampleRef1, sampleRef2));

        sub = new Submission();
        sub.setId("a");
        submissionRepository.insert(sub);

        storedSubmittable.setSubmission(sub);

        SubmittableHelperService.fillInReferences(storedSubmittable);


        repository.insert(storedSubmittable);
    }

    @After
    public void clearUp() {
        repository.deleteAll();
        submissionRepository.deleteAll();
    }

    @Test
    public void referencesPlacedInMap() {
        Map<String, List<AbstractSubsRef>> expected = new HashMap<>();
        expected.put("StudyRef", Arrays.asList(studyRef));
        expected.put("SampleRef", Arrays.asList(sampleRef1, sampleRef2));


        Map<String, List<AbstractSubsRef>> actual = storedSubmittable.getReferences();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void match_one_where_ref_is_accession() {
        List<Analysis> matches = repository.findBySubmissionIdAndReference(sub.getId(), studyRef);

        Assert.assertEquals(1, matches.size());
    }

    @Test
    public void match_one_where_ref_is_alias() {
        List<Analysis> matches = repository.findBySubmissionIdAndReference(sub.getId(), sampleRef1);

        Assert.assertEquals(1, matches.size());
    }

    @Test
    public void match_none_where_ref_is_wrong() {
        SampleRef noMatchRef = new SampleRef();
        noMatchRef.setAccession("BLAHBLAHBLAH");

        List<Analysis> matches = repository.findBySubmissionIdAndReference(sub.getId(), noMatchRef);

        Assert.assertEquals(0, matches.size());
    }

    @Test
    public void match_with_accession_and_alias(){
        SampleRef ref = new SampleRef();
        ref.setTeam(sampleRef1.getTeam());
        ref.setAlias(sampleRef1.getAlias());
        ref.setAccession("anything");

        List<Analysis> matches = repository.findBySubmissionIdAndReference(sub.getId(), ref);

        Assert.assertEquals(1, matches.size());
    }


}
