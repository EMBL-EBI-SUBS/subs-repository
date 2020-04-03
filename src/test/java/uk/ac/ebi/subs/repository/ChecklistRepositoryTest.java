package uk.ac.ebi.subs.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.ac.ebi.subs.repository.model.Checklist;
import uk.ac.ebi.subs.repository.repos.ChecklistRepository;
import uk.ac.ebi.subs.repository.repos.schema.ValidationSchema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableMongoAuditing
public class ChecklistRepositoryTest {

    @Autowired
    private ChecklistRepository checklistRepository;

    private static Map<String, String> SCHEMA_IDS;
    private static final List<String> DATA_TYPE_IDS = List.of("dataTypeId_1", "dataTypeId_2", "dataTypeId_3");
    private static final String VALIDATION_SCHEMA_PATH_AND_PREFIX = "testResources/validationSchemaFor-";
    private static final String VALIDATION_SCHEMA_FILE_EXTENSION = ".json";

    private List<Checklist> checklists = new ArrayList<>();


    @Before
    public void buildUp() {
        SCHEMA_IDS = DATA_TYPE_IDS.stream()
                .collect(Collectors.toMap(dataTypeId -> dataTypeId, dataTypeId -> "schema_for_" + dataTypeId));

        checklistRepository.deleteAll();

        generateMockChecklists();

        checklistRepository.insert(checklists);
    }

    @After
    public void tearDown() {
        checklistRepository.deleteAll();
    }

    @Test
    public void fetchAll() {

        Page<Checklist> checklists = checklistRepository.findAll(new PageRequest(0, 10));

        assertThat(checklists, is(notNullValue()));
        assertThat(checklists.getTotalElements(), is(equalTo(3L)));
        assertThat(checklists.getContent().get(0).getCreatedDate(), is(notNullValue()));
    }

    @Test
    public void whenChecklistExistsForASpecificDataTypeId_ThenQueryReturnsAChecklist() {
        final String exampleDataTypeId = DATA_TYPE_IDS.get(0);
        List<Checklist> checklistPage = checklistRepository.findByDataTypeId(exampleDataTypeId);

        Checklist checklist = checklistPage.iterator().next();

        assertThat(checklist, is(notNullValue()));
        assertThat(checklist.getDataTypeId(), is(equalTo(exampleDataTypeId)));
    }

    @Test
    public void whenChecklistNotExistsForASpecificDataTypeId_ThenQueryResultIsNull() {
        final String nonExistingDataTypeId = "non existing data type ID";
        List<Checklist> emptyChecklistPage = checklistRepository.findByDataTypeId(nonExistingDataTypeId);

        assertThat(emptyChecklistPage.size(), is(equalTo(0)));
    }

    @Test
    public void fetchAllValidationSchemasFromChecklistRepository() throws IOException {
        final List<ValidationSchema> allValidationSchema = checklistRepository.findAllValidationSchema();

        assertThat(allValidationSchema.size(), is(equalTo(3)));
        allValidationSchema.forEach(validationSchema -> {
            String dataTypeId = validationSchema.getDataTypeId();
            final String schemaId = SCHEMA_IDS.get(dataTypeId);
            assertThat(validationSchema.getId(), is(equalTo(schemaId)));
            assertThat(validationSchema.getDisplayName(), is(equalTo("Display name of " + schemaId)));
            assertThat(validationSchema.getDescription(), is(equalTo("Description of " + schemaId)));
        });
    }

    @Test
    public void getSpecificValidationSchema_whenExists() throws IOException {
        final String expectedSchemaId = SCHEMA_IDS.get(DATA_TYPE_IDS.get(0));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode validationSchema = mapper.readTree(checklistRepository.findValidationSchemaById(expectedSchemaId));

        assertThat(validationSchema, is(notNullValue()));
        assertThat(validationSchema.get("id").asText(), is(equalTo(expectedSchemaId)));
    }

    @Test
    public void getSpecificValidationSchema_whenDoesNotExist() throws IOException {
        final String nonExistingSchemaId = "notExists";
        String validationSchemaStr = checklistRepository.findValidationSchemaById(nonExistingSchemaId);

        assertThat(validationSchemaStr, is(nullValue()));
    }

    private void generateMockChecklists() {
        checklists = DATA_TYPE_IDS.stream()
                .map(dataTypeId ->
                        generateChecklist(dataTypeId, SCHEMA_IDS.get(dataTypeId), generateValidationSchema(dataTypeId)))
                .collect(Collectors.toList());
    }

    private Checklist generateChecklist(String dataTypeId, String schemaId, JsonNode validationSchema) {
        Checklist checklist = new Checklist();
        checklist.setId(schemaId);
        checklist.setDataTypeId(dataTypeId);
        checklist.setDisplayName("Display name of " + schemaId);
        checklist.setDescription("Description of " + schemaId);
        checklist.setValidationSchema(validationSchema);

        return checklist;
    }

    private JsonNode generateValidationSchema(String dataTypeId) {
        final String checklistResource = VALIDATION_SCHEMA_PATH_AND_PREFIX + dataTypeId + VALIDATION_SCHEMA_FILE_EXTENSION;
        File validationSchema = new File(ClassLoader.getSystemClassLoader().getResource(checklistResource).getFile());

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readTree(validationSchema);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + checklistResource);
        }
    }

}
