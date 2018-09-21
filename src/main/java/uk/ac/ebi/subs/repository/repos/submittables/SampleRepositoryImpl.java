package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.Sample;
import uk.ac.ebi.subs.repository.repos.submittables.support.SubmittablesAggregateSupport;
import uk.ac.ebi.subs.validator.repository.ValidationResultRepository;

import java.util.List;

@Component
public class SampleRepositoryImpl implements SubmittableRepositoryCustom<Sample> {

    private SubmittablesAggregateSupport<Sample> aggregateSupport;

    public SampleRepositoryImpl(MongoTemplate mongoTemplate, ValidationResultRepository validationResultRepository) {
        this.aggregateSupport = new SubmittablesAggregateSupport<>(mongoTemplate, validationResultRepository, Sample.class);
    }

    @Override
    public Page<Sample> submittablesInTeam(String teamName, Pageable pageable) {
        return aggregateSupport.itemsByTeam(teamName, pageable);
    }

    @Override
    public Page<Sample> submittablesInTeams(List<String> teamNames, Pageable pageable) {
        return aggregateSupport.itemsByTeams(teamNames, pageable);
    }

    @Override
    public Page<Sample> findBySubmissionIdAndDataTypeIdWithErrors(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithErrors(submissionId, dataTypeId, pageable);
    }

    @Override
    public Page<Sample> findBySubmissionIdAndDataTypeIdWithWarnings(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithWarnings(submissionId, dataTypeId, pageable);
    }
}
