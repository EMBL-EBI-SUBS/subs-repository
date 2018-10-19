package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.repository.model.EgaDacPolicy;
import uk.ac.ebi.subs.repository.repos.submittables.support.SubmittablesAggregateSupport;
import uk.ac.ebi.subs.validator.repository.ValidationResultRepository;

import java.util.List;

@Component
public class EgaDacPolicyRepositoryImpl implements SubmittableRepositoryCustom<EgaDacPolicy> {

    private SubmittablesAggregateSupport<EgaDacPolicy> aggregateSupport;

    public EgaDacPolicyRepositoryImpl(MongoTemplate mongoTemplate, ValidationResultRepository validationResultRepository) {
        this.aggregateSupport = new SubmittablesAggregateSupport<>(mongoTemplate, validationResultRepository, EgaDacPolicy.class);
    }

    @Override
    public Page<EgaDacPolicy> submittablesInTeam(String teamName, Pageable pageable) {
        return aggregateSupport.itemsByTeam(teamName, pageable);
    }

    @Override
    public Page<EgaDacPolicy> submittablesInTeams(List<String> teamNames, Pageable pageable) {
        return aggregateSupport.itemsByTeams(teamNames, pageable);
    }

    @Override
    public Page<EgaDacPolicy> findBySubmissionIdAndDataTypeIdWithErrors(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithErrors(submissionId, dataTypeId, pageable);
    }

    @Override
    public Page<EgaDacPolicy> findBySubmissionIdAndDataTypeIdWithWarnings(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithWarnings(submissionId, dataTypeId, pageable);
    }

    @Override
    public List<EgaDacPolicy> findBySubmissionIdAndReference(String submissionId, AbstractSubsRef ref) {
        return aggregateSupport.findBySubmissionIdAndReference(submissionId, ref);
    }
}
