package uk.ac.ebi.subs.repository.config;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    @Data(staticConstructor = "of")
    static class RepoTypeRefConfig<T extends StoredSubmittable> {
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

        if (repositoryConfig.isAnalysisEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            Analysis.class,
                            AnalysisRef.class,
                            analysisRepository
                    )
            );
        }

        if (repositoryConfig.isAssayDataEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            AssayData.class,
                            AssayDataRef.class,
                            assayDataRepository
                    )
            );
        }

        if (repositoryConfig.isAssayEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            Assay.class,
                            AssayRef.class,
                            assayRepository
                    )
            );
        }

        if (repositoryConfig.isEgaDacPolicyEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            EgaDacPolicy.class,
                            EgaDacPolicyRef.class,
                            egaDacPolicyRepository
                    )
            );
        }

        if (repositoryConfig.isEgaDacEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            EgaDac.class,
                            EgaDacRef.class,
                            egaDacRepository
                    )
            );
        }

        if (repositoryConfig.isEgaDatasetEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            EgaDataset.class,
                            EgaDatasetRef.class,
                            egaDatasetRepository
                    )
            );
        }

        if (repositoryConfig.isProjectEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            Project.class,
                            ProjectRef.class,
                            projectRepository
                    )
            );
        }

        if (repositoryConfig.isProtocolEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            Protocol.class,
                            ProtocolRef.class,
                            protocolRepository
                    )
            );
        }

        if (repositoryConfig.isSampleGroupEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            SampleGroup.class,
                            SampleGroupRef.class,
                            sampleGroupRepository
                    )
            );
        }

        if (repositoryConfig.isSampleEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            Sample.class,
                            SampleRef.class,
                            sampleRepository
                    )
            );
        }

        if (repositoryConfig.isStudyEnabled()) {
            configList.add(
                    RepoTypeRefConfig.of(
                            Study.class,
                            StudyRef.class,
                            studyRepository
                    )
            );
        }

        return Collections.unmodifiableList(configList);
    }


    @Bean
    public List<SubmittableRepository<?>> submissionContentsRepositories(List<RepoTypeRefConfig<?>> availableRepoConfig) {
        return Collections.unmodifiableList(
                availableRepoConfig
                        .stream()
                        .map(RepoTypeRefConfig::getRepository)
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
