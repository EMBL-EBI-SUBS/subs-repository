package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.subs.data.component.Archive;
import uk.ac.ebi.subs.data.submittable.Assay;
import uk.ac.ebi.subs.data.submittable.AssayData;
import uk.ac.ebi.subs.data.submittable.Project;
import uk.ac.ebi.subs.data.submittable.Sample;
import uk.ac.ebi.subs.repository.model.DataType;
import uk.ac.ebi.subs.repository.repos.DataTypeRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataTypeRepositoryTest {

    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Before
    public void setup() {
        createAndPersistDataType(Archive.BioSamples, "Samples", "Sample", Sample.class.getSimpleName());
        createAndPersistDataType(Archive.BioStudies, "Projects", "Project", Project.class.getSimpleName());
        createAndPersistDataType(Archive.Ena, "Assays", "Assay", Assay.class.getSimpleName());
        createAndPersistDataType(Archive.Ena, "List of Assay Data", "Assay Data", AssayData.class.getSimpleName());
    }

    private void createAndPersistDataType(Archive archive, String pluralName, String singularName, String submittableName) {
        DataType dataType = new DataType();
        dataType.setArchive(archive);
        dataType.setDisplayNamePlural(pluralName);
        dataType.setDisplayNameSingular(singularName);
        dataType.setSubmittableClassName(submittableName);

        dataTypeRepository.save(dataType);
    }

    @After
    public void tearDown() {
        dataTypeRepository.deleteAll();
    }

    @Test
    public void when2DataTypeRegisteredForAnArchive_thenUsingFindByArchive_returnsThat2() {
        List<DataType> dataTypes = dataTypeRepository.findByArchive(Archive.Ena);

        assertThat(dataTypes.size(), is(equalTo(2)));
    }
}
