package uk.ac.ebi.subs.repository.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.data.component.AnalysisRef;
import uk.ac.ebi.subs.data.component.AssayDataRef;
import uk.ac.ebi.subs.data.component.AssayRef;
import uk.ac.ebi.subs.data.component.EgaDacPolicyRef;
import uk.ac.ebi.subs.data.component.EgaDacRef;
import uk.ac.ebi.subs.data.component.EgaDatasetRef;
import uk.ac.ebi.subs.data.component.ProjectRef;
import uk.ac.ebi.subs.data.component.ProtocolRef;
import uk.ac.ebi.subs.data.component.SampleGroupRef;
import uk.ac.ebi.subs.data.component.SampleRef;
import uk.ac.ebi.subs.data.component.SampleRelationship;
import uk.ac.ebi.subs.data.component.StudyRef;
import uk.ac.ebi.subs.repository.model.Analysis;
import uk.ac.ebi.subs.repository.model.Assay;
import uk.ac.ebi.subs.repository.model.AssayData;
import uk.ac.ebi.subs.repository.model.EgaDac;
import uk.ac.ebi.subs.repository.model.EgaDacPolicy;
import uk.ac.ebi.subs.repository.model.EgaDataset;
import uk.ac.ebi.subs.repository.model.Project;
import uk.ac.ebi.subs.repository.model.Protocol;
import uk.ac.ebi.subs.repository.model.Sample;
import uk.ac.ebi.subs.repository.model.SampleGroup;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;
import uk.ac.ebi.subs.repository.model.Study;
import uk.ac.ebi.subs.repository.repos.DataTypeRepository;
import uk.ac.ebi.subs.repository.repos.submittables.AnalysisRepository;
import uk.ac.ebi.subs.repository.repos.submittables.AssayDataRepository;
import uk.ac.ebi.subs.repository.repos.submittables.AssayRepository;
import uk.ac.ebi.subs.repository.repos.submittables.EgaDacPolicyRepository;
import uk.ac.ebi.subs.repository.repos.submittables.EgaDacRepository;
import uk.ac.ebi.subs.repository.repos.submittables.EgaDatasetRepository;
import uk.ac.ebi.subs.repository.repos.submittables.ProjectRepository;
import uk.ac.ebi.subs.repository.repos.submittables.ProtocolRepository;
import uk.ac.ebi.subs.repository.repos.submittables.SampleGroupRepository;
import uk.ac.ebi.subs.repository.repos.submittables.SampleRepository;
import uk.ac.ebi.subs.repository.repos.submittables.StudyRepository;
import uk.ac.ebi.subs.repository.repos.submittables.SubmittableRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RepositoryMappingProperties.class)
public class SubmittableConfig {

    private final Logger logger = LoggerFactory.getLogger(SubmittableConfig.class);

    @Data(staticConstructor = "of")
    public static class RepoTypeRefConfig<T extends StoredSubmittable> {
        @NonNull
        private final Class<T> submittableClass;
        @NonNull
        private final List<String> dataTypes;
        @NonNull
        private final Class<? extends AbstractSubsRef> refClass;
        @NonNull
        private final SubmittableRepository<T> repository;
    }

    @Bean
    public List<RepoTypeRefConfig<?>> availableRepoConfig() {
        List<RepoTypeRefConfig<?>> configList = new ArrayList<>();

        logger.debug("Generating RepTypeRefConfig list");
        logger.debug("Using config {}",this.repositoryConfig);

        if (repositoryConfig.isAnalysisEnabled()) {
            logger.info("Analysis repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            Analysis.class,
                            repositoryMappingProperties.getAnalysis(),
                            AnalysisRef.class,
                            analysisRepository
                    )
            );
        }

        if (repositoryConfig.isAssayDataEnabled()) {
            logger.info("Assay data repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            AssayData.class,
                            repositoryMappingProperties.getAssayData(),
                            AssayDataRef.class,
                            assayDataRepository
                    )
            );
        }

        if (repositoryConfig.isAssayEnabled()) {
            logger.info("Assay repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            Assay.class,
                            repositoryMappingProperties.getAssay(),
                            AssayRef.class,
                            assayRepository
                    )
            );
        }

        if (repositoryConfig.isEgaDacPolicyEnabled()) {
            logger.info("Ega Dac Policy repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            EgaDacPolicy.class,
                            repositoryMappingProperties.getEgaDacPolicy(),
                            EgaDacPolicyRef.class,
                            egaDacPolicyRepository
                    )
            );
        }

        if (repositoryConfig.isEgaDacEnabled()) {
            logger.info("Ega Dac repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            EgaDac.class,
                            repositoryMappingProperties.getEgaDac(),
                            EgaDacRef.class,
                            egaDacRepository
                    )
            );
        }

        if (repositoryConfig.isEgaDatasetEnabled()) {
            logger.info("Ega Dataset repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            EgaDataset.class,
                            repositoryMappingProperties.getEgaDataset(),
                            EgaDatasetRef.class,
                            egaDatasetRepository
                    )
            );
        }

        if (repositoryConfig.isProjectEnabled()) {
            logger.info("Project repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            Project.class,
                            repositoryMappingProperties.getProject(),
                            ProjectRef.class,
                            projectRepository
                    )
            );
        }

        if (repositoryConfig.isProtocolEnabled()) {
            logger.info("Protocol repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            Protocol.class,
                            repositoryMappingProperties.getProtocol(),
                            ProtocolRef.class,
                            protocolRepository
                    )
            );
        }

        if (repositoryConfig.isSampleGroupEnabled()) {
            logger.info("Sample Group repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            SampleGroup.class,
                            Collections.emptyList(),
                            SampleGroupRef.class,
                            sampleGroupRepository
                    )
            );
        }

        if (repositoryConfig.isSampleEnabled()) {
            logger.info("Sample repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            Sample.class,
                            repositoryMappingProperties.getSample(),
                            SampleRef.class,
                            sampleRepository
                    )
            );
            configList.add(
                    RepoTypeRefConfig.of(
                            Sample.class,
                            repositoryMappingProperties.getSample(),
                            SampleRelationship.class,
                            sampleRepository
                    )
            );
        }

        if (repositoryConfig.isStudyEnabled()) {
            logger.info("Study repo exposed");
            configList.add(
                    RepoTypeRefConfig.of(
                            Study.class,
                            repositoryMappingProperties.getStudy(),
                            StudyRef.class,
                            studyRepository
                    )
            );
        }

        logger.debug("Repository configuration list contains: {}", configList);

        return Collections.unmodifiableList(configList);
    }

    @Bean
    public List<SubmittableRepository<?>> submissionContentsRepositories(List<RepoTypeRefConfig<?>> availableRepoConfig) {
        return Collections.unmodifiableList(
                availableRepoConfig
                        .stream()
                        .map(RepoTypeRefConfig::getRepository)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    @Bean
    public Map<Class<? extends AbstractSubsRef>, SubmittableRepository<? extends StoredSubmittable>> referenceRepositoryMap(List<RepoTypeRefConfig<?>> availableRepoConfig) {
        Map<Class<? extends AbstractSubsRef>, SubmittableRepository<? extends StoredSubmittable>>
                referenceRepositoryMap = new HashMap<>();

        availableRepoConfig.stream().forEach(repoTypeRefConfig ->
                referenceRepositoryMap.put(
                        repoTypeRefConfig.getRefClass(),
                        repoTypeRefConfig.getRepository()
                )
        );

        return Collections.unmodifiableMap(referenceRepositoryMap);
    }

    @Bean
    public Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap(List<RepoTypeRefConfig<?>> availableRepoConfig) {
        Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> map = new HashMap<>();

        availableRepoConfig.stream().forEach(repoTypeRefConfig ->
                map.put(
                        repoTypeRefConfig.getSubmittableClass(),
                        repoTypeRefConfig.getRepository()
                )
        );


        return Collections.unmodifiableMap(map);
    }

    @Bean
    public DataTypeRepositoryMap dataTypeRepositoryMap(List<RepoTypeRefConfig<?>> availableRepoConfig) {
        DataTypeRepositoryMap map = new DataTypeRepositoryMap();

        availableRepoConfig.forEach(repoTypeRefConfig ->
                repoTypeRefConfig.getDataTypes().forEach( dataType ->
                        map.getDataTypeRepositoryMap().put(
                                dataType,
                                repoTypeRefConfig.getRepository()
                        )
                )
        );

        return map;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataTypeRepositoryMap {
        Map<String, SubmittableRepository<? extends StoredSubmittable>> dataTypeRepositoryMap = new HashMap<>();
    }

    @Bean
    public List<Class<? extends StoredSubmittable>> submittablesClassList(List<RepoTypeRefConfig<?>> availableRepoConfig) {
        return Collections.unmodifiableList(
                availableRepoConfig
                        .stream()
                        .map(RepoTypeRefConfig::getSubmittableClass)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    @NonNull
    private UsiRepositoryConfig repositoryConfig;

    @NonNull
    private AnalysisRepository analysisRepository;
    @NonNull
    private AssayDataRepository assayDataRepository;
    @NonNull
    private AssayRepository assayRepository;
    @NonNull
    private EgaDacPolicyRepository egaDacPolicyRepository;
    @NonNull
    private EgaDacRepository egaDacRepository;
    @NonNull
    private EgaDatasetRepository egaDatasetRepository;
    @NonNull
    private ProjectRepository projectRepository;
    @NonNull
    private ProtocolRepository protocolRepository;
    @NonNull
    private SampleGroupRepository sampleGroupRepository;
    @NonNull
    private SampleRepository sampleRepository;
    @NonNull
    private StudyRepository studyRepository;

    @NonNull
    private DataTypeRepository dataTypeRepository;

    @NonNull
    private RepositoryMappingProperties repositoryMappingProperties;
}
