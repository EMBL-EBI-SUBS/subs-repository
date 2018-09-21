package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.Project;
import uk.ac.ebi.subs.repository.repos.submittables.support.SubmittablesAggregateSupport;
import uk.ac.ebi.subs.validator.repository.ValidationResultRepository;

import java.util.List;

@Component
public class ProjectRepositoryImpl implements SubmittableRepositoryCustom<Project> {

    private SubmittablesAggregateSupport<Project> aggregateSupport;

    public ProjectRepositoryImpl(MongoTemplate mongoTemplate, ValidationResultRepository validationResultRepository) {
        this.aggregateSupport = new SubmittablesAggregateSupport<>(mongoTemplate, validationResultRepository, Project.class);
    }

    @Override
    public Page<Project> submittablesInTeam(String teamName, Pageable pageable) {
        return aggregateSupport.itemsByTeam(teamName, pageable);
    }

    @Override
    public Page<Project> submittablesInTeams(List<String> teamNames, Pageable pageable) {
        return aggregateSupport.itemsByTeams(teamNames, pageable);
    }

    @Override
    public Page<Project> findBySubmissionIdAndDataTypeIdWithErrors(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithErrors(submissionId, dataTypeId, pageable);
    }

    @Override
    public Page<Project> findBySubmissionIdAndDataTypeIdWithWarnings(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithWarnings(submissionId, dataTypeId, pageable);
    }
}
