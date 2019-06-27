package uk.ac.ebi.subs.repository.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "usi.archive.dispatcher")
class ArchiveProperties {
    private List<String> enabled;
    private Map<String, List<String>> dataTypes;

    List<String> enabledDataTypeNames() {
        List<String> dataTypeNames = new ArrayList<>();
        for (String archiveName : enabled) {
            dataTypeNames.addAll(dataTypes.get(enabled));
        }

        return dataTypeNames;
    }
}