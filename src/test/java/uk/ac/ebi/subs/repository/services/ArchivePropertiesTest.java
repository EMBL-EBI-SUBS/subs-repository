package uk.ac.ebi.subs.repository.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ArchivePropertiesTest {

    private ArchiveProperties archiveProperties = new ArchiveProperties();

    @Test
    public void testEnabledataTypeNames() {
        List<String> dataTypeNames = Arrays.asList("samples", "projects");
        archiveProperties.setEnabled(Arrays.asList("BioSamples", "BioStudies"));
        Map<String, List<String>> dataTypes = new HashMap<>();
        dataTypes.put("BioSamples", Collections.singletonList(dataTypeNames.get(0)));
        dataTypes.put("BioStudies", Collections.singletonList(dataTypeNames.get(1)));
        archiveProperties.setDataTypes(dataTypes);

        assertThat(archiveProperties.enabledDataTypeNames(), is(dataTypeNames));
    }
}
