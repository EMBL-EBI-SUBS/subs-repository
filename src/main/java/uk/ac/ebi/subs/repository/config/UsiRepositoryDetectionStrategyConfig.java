package uk.ac.ebi.subs.repository.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;
import uk.ac.ebi.subs.repository.repos.submittables.SubmittableRepository;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
/**
 * Configure which repositories are exposed by Spring Data Rest, so that the properties in UsiepositoryConfig
 * are respected.
 *
 * This supplements the config in spring.data.rest.detection-strategy=?
 *
 */
public class UsiRepositoryDetectionStrategyConfig extends RepositoryRestConfigurerAdapter {


    private final Logger logger = LoggerFactory.getLogger(UsiRepositoryDetectionStrategyConfig.class);

    @NonNull
    private Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        RepositoryDetectionStrategy rds = config.getRepositoryDetectionStrategy();

        config.setRepositoryDetectionStrategy(
                repositoryDetectionStrategy(rds)
        );
    }


    private RepositoryDetectionStrategy repositoryDetectionStrategy(
            RepositoryDetectionStrategy repositoryDetectionStrategy) {
        RepositoryDetectionStrategy rds = new RepositoryDetectionStrategy() {
            @Override
            public boolean isExported(RepositoryMetadata metadata) {

                boolean isStoredSubmittable = StoredSubmittable.class.isAssignableFrom(metadata.getDomainType());
                boolean isSubmittableRepoEnabled = submittableRepositoryMap.containsKey(metadata.getDomainType());

                boolean isExportedByStandardStrategy = repositoryDetectionStrategy.isExported(metadata);


                boolean isExported = (isStoredSubmittable && !isSubmittableRepoEnabled) ?
                        false : isExportedByStandardStrategy;

                logger.debug(
                        "Resource: {} isStoredSubmittable: {} isSubmittableRepoEnabled: {} isExportedByStandardStrategy: {} isExported: {}",
                        metadata.getDomainType(),
                        isStoredSubmittable,
                        isSubmittableRepoEnabled,
                        isExportedByStandardStrategy,
                        isExported
                );

                return isExported;
            }
        };

        return rds;
    }
}
