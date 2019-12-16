package uk.ac.ebi.subs.repository.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "usi.repository-mapping")
public class RepositoryMappingProperties {

    private List<String> analysis;
    private List<String> assayData;
    private List<String> assay;
    private List<String> egaDacPolicy;
    private List<String> egaDac;
    private List<String> egaDataset;
    private List<String> project;
    private List<String> protocol;
    private List<String> sampleGroup;
    private List<String> sample;
    private List<String> study;
}
