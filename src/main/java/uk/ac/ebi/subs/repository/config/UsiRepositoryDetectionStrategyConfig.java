package uk.ac.ebi.subs.repository.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;
import uk.ac.ebi.subs.repository.repos.submittables.SubmittableRepository;

import java.util.Map;

@Configuration

public class UsiRepositoryDetectionStrategyConfig extends RepositoryRestConfigurerAdapter {


    @Autowired Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        RepositoryDetectionStrategy rds = config.getRepositoryDetectionStrategy();

        config.setRepositoryDetectionStrategy(
                repositoryDetectionStrategy(rds)
        );


    }


    private RepositoryDetectionStrategy repositoryDetectionStrategy(
            RepositoryDetectionStrategy repositoryDetectionStrategy){
        RepositoryDetectionStrategy rds = new RepositoryDetectionStrategy(){
            @Override
            public boolean isExported(RepositoryMetadata metadata) {

                boolean isStoredSubmittableClass = StoredSubmittable.class.isAssignableFrom(metadata.getDomainType());
                boolean isInSubmittableRepositoryMap = submittableRepositoryMap.containsKey(metadata.getDomainType());

                boolean isExported = (isStoredSubmittableClass && !isInSubmittableRepositoryMap) ? false :
                        repositoryDetectionStrategy.isExported(metadata);

                return isExported;
            }
        };

        return rds;
    }
}
