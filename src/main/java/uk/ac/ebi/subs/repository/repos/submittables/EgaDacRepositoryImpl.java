package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.repository.model.EgaDac;
import uk.ac.ebi.subs.repository.repos.submittables.support.SubmittablesAggregateSupport;
import uk.ac.ebi.subs.validator.repository.ValidationResultRepository;

import java.util.List;

@Component
public class EgaDacRepositoryImpl implements SubmittableRepositoryCustom<EgaDac> {

    private SubmittablesAggregateSupport<EgaDac> aggregateSupport;

    public EgaDacRepositoryImpl(MongoTemplate mongoTemplate, ValidationResultRepository validationResultRepository) {
        this.aggregateSupport = new SubmittablesAggregateSupport<>(mongoTemplate, validationResultRepository, EgaDac.class);
    }

    @Override
    public Page<EgaDac> submittablesInTeam(String teamName, Pageable pageable) {
        return aggregateSupport.itemsByTeam(teamName, pageable);
    }

    @Override
    public Page<EgaDac> submittablesInTeams(List<String> teamNames, Pageable pageable) {
        return aggregateSupport.itemsByTeams(teamNames, pageable);
    }

    @Override
    public Page<EgaDac> findBySubmissionIdAndDataTypeIdWithErrors(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithErrors(submissionId, dataTypeId, pageable);
    }

    @Override
    public Page<EgaDac> findBySubmissionIdAndDataTypeIdWithWarnings(String submissionId, String dataTypeId, Pageable pageable) {
        return aggregateSupport.submittablesByDataTypeWithWarnings(submissionId, dataTypeId, pageable);
    }

    @Override
    public List<EgaDac> findBySubmissionIdAndReference(String submissionId, AbstractSubsRef ref) {
        return aggregateSupport.findBySubmissionIdAndReference(submissionId, ref);
    }
}
