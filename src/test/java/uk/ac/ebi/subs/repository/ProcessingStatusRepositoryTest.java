package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.subs.data.component.Archive;
import uk.ac.ebi.subs.data.status.ProcessingStatusEnum;
import uk.ac.ebi.subs.repository.model.DataType;
import uk.ac.ebi.subs.repository.model.ProcessingStatus;
import uk.ac.ebi.subs.repository.repos.DataTypeRepository;
import uk.ac.ebi.subs.repository.repos.status.ProcessingStatusRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProcessingStatusRepositoryTest {

    @Autowired
    private ProcessingStatusRepository processingStatusRepository;

    @Autowired
    private DataTypeRepository dataTypeRepository;

    @After
    public void buildUp() {
        processingStatusRepository.deleteAll();
    }

    @Test
    public void testAggregationWithNoData() {
        Map<String,Integer> statusCounts = processingStatusRepository.summariseSubmissionStatus("foo");
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
                processingStatusRepository.insert(status(entry.getKey(),"foo","sample"));
            }
        }

        Map<String,Integer> statusCounts = processingStatusRepository.summariseSubmissionStatus("foo");
        assertThat(statusCounts,notNullValue());

        assertThat(statusCounts,is(equalTo(expected)));
    }

    @Test
    public void testTypeStatusAggregation() {
        Map<String,Map<String,Integer>> expected = new HashMap<>();
        expected.put("sample",new HashMap<>());
        expected.put("study",new HashMap<>());
        expected.get("sample").put("good",100);
        expected.get("sample").put("bad",2);
        expected.get("study").put("tolerable",42);

        for (Map.Entry<String,Map<String,Integer>> typeEntry : expected.entrySet() ) {
            String type = typeEntry.getKey();

            for (Map.Entry<String, Integer> statusCountEntry : typeEntry.getValue().entrySet()) {
                for (int i = 0; i < statusCountEntry.getValue(); i++) {
                    ProcessingStatus status = status(statusCountEntry.getKey(), "foo", type);
                    processingStatusRepository.insert(status);
                }
            }
        }

        Map<String,Map<String,Integer>> statusCounts = processingStatusRepository.summariseSubmissionStatusAndType("foo");
        assertThat(statusCounts,notNullValue());

        assertThat(statusCounts,is(equalTo(expected)));
    }

    @Test
    public void testDataTypeAggregation() {
        Set<String> processingStatusesToAllow = setupStatusesToProcess();

        DataType sampleDataType = createDataType("Sample", Archive.BioSamples, "Sample");
        DataType projectDataType = createDataType("Project", Archive.BioStudies, "Project");
        DataType studyDataType = createDataType("Study", Archive.Ena, "Study");

        createProcessingStatus(sampleDataType, "testSubId", UUID.randomUUID().toString());
        createProcessingStatus(sampleDataType, "testSubId", UUID.randomUUID().toString());
        createProcessingStatus(projectDataType, "testSubId", UUID.randomUUID().toString());
        createProcessingStatus(studyDataType, "testSubId", UUID.randomUUID().toString());

        Map<DataType, Set<String>> dataTypeWithSubmittableIds = processingStatusRepository.summariseDataTypesWithSubmittableIds("testSubId", processingStatusesToAllow);

        assertThat(dataTypeWithSubmittableIds.size(), is(equalTo(3)));
        assertThat(dataTypeWithSubmittableIds.containsKey(sampleDataType), is(true));
        assertThat(dataTypeWithSubmittableIds.get(sampleDataType).size(), is(equalTo(2)));
        assertThat(dataTypeWithSubmittableIds.containsKey(projectDataType), is(true));
        assertThat(dataTypeWithSubmittableIds.get(projectDataType).size(), is(equalTo(1)));
        assertThat(dataTypeWithSubmittableIds.containsKey(studyDataType), is(true));
        assertThat(dataTypeWithSubmittableIds.get(studyDataType).size(), is(equalTo(1)));
    }

    private void createProcessingStatus(DataType sampleDataType, String submissionId, String submittableId) {
        ProcessingStatus processingStatus = new ProcessingStatus();
        processingStatus.setDataType(sampleDataType);
        processingStatus.setStatus(ProcessingStatusEnum.Draft);
        processingStatus.setArchive(sampleDataType.getArchive().name());
        processingStatus.setSubmissionId(submissionId);
        processingStatus.setSubmittableId(submittableId);
        processingStatus.setSubmittableType(sampleDataType.getSubmittableClassName());
        processingStatusRepository.save(processingStatus);
    }

    private ProcessingStatus status(String status, String subId, String type) {
        ProcessingStatus ps = new ProcessingStatus();

        ps.setStatus(status);
        ps.setSubmissionId(subId);
        ps.setSubmittableType(type);

        return ps;
    }

    private Set<String> setupStatusesToProcess() {
        Set<String> processingStatusesToAllow = new HashSet<>();
        processingStatusesToAllow.add(ProcessingStatusEnum.Draft.name());
        processingStatusesToAllow.add(ProcessingStatusEnum.Submitted.name());

        return processingStatusesToAllow;
    }
    private DataType createDataType(String dataTypeId, Archive archiveName, String submittsbleClassName) {
        DataType dataType = new DataType();
        dataType.setId(dataTypeId);
        dataType.setArchive(archiveName);
        dataType.setSubmittableClassName(submittsbleClassName);

        return dataTypeRepository.save(dataType);
    }

}
