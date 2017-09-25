package uk.ac.ebi.subs.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("usi.repository.config")
@Data
public class UsiRepositoryConfig {
    private boolean analysisEnabled = true;
    private boolean assayDataEnabled = true;
    private boolean assayEnabled = true;
    private boolean egaDacPolicyEnabled = true;
    private boolean egaDacEnabled = true;
    private boolean egaDatasetEnabled = true;
    private boolean projectEnabled = true;
    private boolean protocolEnabled = true;
    private boolean sampleGroupEnabled = true;
    private boolean sampleEnabled = true;
    private boolean studyEnabled = true;
}
