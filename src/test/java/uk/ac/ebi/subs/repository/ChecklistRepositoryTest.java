package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import uk.ac.ebi.subs.repository.model.Checklist;
import uk.ac.ebi.subs.repository.repos.ChecklistRepository;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableMongoAuditing
public class ChecklistRepositoryTest {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Before
    public void buildUp() {
        checklistRepository.deleteAll();

        checklistRepository.insert(generateChecklist());
    }

    private Checklist generateChecklist() {
        Checklist checklist = new Checklist();
        checklist.setId(UUID.randomUUID().toString());
        checklist.setDisplayName("Test Checklist");
        checklist.setDescription("This is an example checklist.");
        checklist.setDataTypeId("samples");

        return checklist;
    }

    @After
    public void tearDown() {
        checklistRepository.deleteAll();
    }

    @Test
    public void fetchAll() {

        Page<Checklist> checklists = checklistRepository.findAll(PageRequest.of(0, 10));

        assertThat(checklists, is(notNullValue()));
        assertThat(checklists.getTotalElements(), is(equalTo(1L)));
        assertThat(checklists.getContent().get(0).getCreatedDate(), is(notNullValue()));
    }

    @Test
    public void whenChecklistExistsForASpecificDataTypeId_ThenQueryReturnsAChecklist() {
        final String samplesDataTypeId = "samples";
        List<Checklist> samplesChecklistPage = checklistRepository.findByDataTypeId(samplesDataTypeId);

        Checklist samplesChecklist = samplesChecklistPage.iterator().next();

        assertThat(samplesChecklist, is(notNullValue()));
        assertThat(samplesChecklist.getDataTypeId(), is(equalTo(samplesDataTypeId)));
    }

    @Test
    public void whenChecklistNotExistsForASpecificDataTypeId_ThenQueryResultIsNull() {
        final String nonExistingDataTypeId = "non existing data type ID";
        List<Checklist> emptyChecklistPage = checklistRepository.findByDataTypeId(nonExistingDataTypeId);

        assertThat(emptyChecklistPage.size(), is(equalTo(0)));
    }


}
