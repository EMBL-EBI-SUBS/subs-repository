package uk.ac.ebi.subs.repository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.number.OrderingComparison;
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
import uk.ac.ebi.subs.repository.model.ArchivedChecklist;
import uk.ac.ebi.subs.repository.model.Checklist;
import uk.ac.ebi.subs.repository.repos.ArchivedChecklistRepository;
import uk.ac.ebi.subs.repository.repos.ChecklistRepository;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableMongoAuditing
public class ArchivedChecklistRepositoryTest {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ArchivedChecklistRepository archivedChecklistRepository;

    @Before
    public void buildUp() {
        checklistRepository.deleteAll();
        archivedChecklistRepository.deleteAll();
    }

    @After
    public void tearDown() {
        checklistRepository.deleteAll();
        archivedChecklistRepository.deleteAll();
    }

    @Test
    public void test() {
        Checklist original = new Checklist();
        original.setId("cl-01");
        original.setDisplayName("Test Checklist");
        original.setDescription("This is an example checklist.");
        original.setDataTypeId("samples");

        checklistRepository.save(original);

        assertThat(original.getCreatedDate(), is(notNullValue()));
        assertThat(original.getLastModifiedDate(), is(notNullValue()));

        Checklist updated = checklistRepository.findById(original.getId()).orElse(null);
        updated.setVersion(original.getVersion() + 1);

        checklistRepository.save(updated);

        assertThat(updated.getLastModifiedDate(), is(OrderingComparison.greaterThan(original.getLastModifiedDate())));

        ArchivedChecklist archivedChecklist = new ArchivedChecklist();
        archivedChecklist.setId(UUID.randomUUID().toString());
        archivedChecklist.setChecklist(original);

        archivedChecklistRepository.save(archivedChecklist);

        archivedChecklist = archivedChecklistRepository.findById(archivedChecklist.getId()).orElse(null);

        assertThat(archivedChecklist, is(notNullValue()));
        assertThat(archivedChecklist.getCreatedDate(), is(notNullValue()));
        assertThat(archivedChecklist.getChecklist().getCreatedDate(), is(equalTo(updated.getCreatedDate())));
    }
}
