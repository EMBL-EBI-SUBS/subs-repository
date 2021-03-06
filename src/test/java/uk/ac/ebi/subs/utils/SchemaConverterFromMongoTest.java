package uk.ac.ebi.subs.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.ac.ebi.subs.repository.util.SchemaConverterFromMongo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class SchemaConverterFromMongoTest {

    private ObjectMapper mapper = new ObjectMapper();
    private SimpleModule module = new SimpleModule();

    private ClassLoader classLoader = this.getClass().getClassLoader();

    @Before
    public void setup() {
        this.mapper.registerModule(module);
    }

    @Test
    public void whenParseSimpleJSONWithSpecificKeys_ConversionWorks() throws IOException {
        File mongoFile = fileForResource("mongo_samples_datatype.json");
        JsonNode jsonFromMongo = new ObjectMapper().readTree(mongoFile);

        File convertedFile = fileForResource("converted_samples_datatype.json");

        JsonNode expectedJson = new ObjectMapper().readTree(convertedFile);

        JsonNode convertedJson = SchemaConverterFromMongo.convertJsonNode(jsonFromMongo);

        assertThat(convertedJson, is(equalTo(expectedJson)));
    }

    @Test
    public void whenParseJSONWStringithSpecificKeys_ConversionWorks() throws IOException {
        File mongoFile = fileForResource("mongo_samples_datatype.json");
        String jsonStringFromMongo = String.join("\n", Files.readAllLines(mongoFile.toPath()));

        File convertedFile = fileForResource("converted_samples_datatype.json");
        JsonNode expectedJson = new ObjectMapper().readTree(convertedFile);
        JsonNode convertedJson = SchemaConverterFromMongo.fixStoredJson(jsonStringFromMongo);

        assertThat(convertedJson, is(equalTo(expectedJson)));
    }

    private File fileForResource(String resourceName){
        return new File(
                this.classLoader.getResource(resourceName)
                        .getFile() //actually a String, rather than a File
        );
    }
}
