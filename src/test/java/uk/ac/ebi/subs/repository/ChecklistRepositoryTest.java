package uk.ac.ebi.subs.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import uk.ac.ebi.subs.repository.model.Checklist;
import uk.ac.ebi.subs.repository.repos.ChecklistRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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

        Page<Checklist> checklists = checklistRepository.findAll(new PageRequest(0, 10));

        Assert.notNull(checklists);
        Assert.isTrue(checklists.getTotalElements() == 1L);
        System.out.println(checklists.getContent());
    }

    @Test
    public void whenChecklistExistsForASpecificDataTypeId_ThenQueryReturnsAChecklist() {
        final String samplesDataTypeId = "samples";
        Checklist samplesChecklist = checklistRepository.findByDataTypeId(samplesDataTypeId);

        assertThat(samplesChecklist, is(notNullValue()));
        assertThat(samplesChecklist.getDataTypeId(), is(equalTo(samplesDataTypeId)));
    }

    @Test
    public void whenChecklistNotExistsForASpecificDataTypeId_ThenQueryResultIsNull() {
        final String nonExistingDataTypeId = "non existing data type ID";
        Checklist nullChecklist = checklistRepository.findByDataTypeId(nonExistingDataTypeId);

        assertThat(nullChecklist, is(nullValue()));
    }


}
