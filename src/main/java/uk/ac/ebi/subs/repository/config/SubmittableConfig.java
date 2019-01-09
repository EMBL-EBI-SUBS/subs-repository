package uk.ac.ebi.subs.repository.config;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.subs.data.component.*;
import uk.ac.ebi.subs.repository.model.*;
import uk.ac.ebi.subs.repository.repos.submittables.*;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SubmittableConfig {


    private final Logger logger = LoggerFactory.getLogger(SubmittableConfig.class);

    @Data(staticConstructor = "of")
    public static class RepoTypeRefConfig<T extends StoredSubmittable> {
        @NonNull
        private final Class<T> submittableClass;
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
                            SampleRef.class,
                            sampleRepository
                    )
            );
            configList.add(
                    RepoTypeRefConfig.of(
                            Sample.class,
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
                            StudyRef.class,
                            studyRepository
                    )
            );
        }

        logger.debug("Submittables configuration list produed: {}",configList);

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
}
