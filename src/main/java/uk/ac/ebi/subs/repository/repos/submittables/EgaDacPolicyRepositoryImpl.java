package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.EgaDacPolicy;
import uk.ac.ebi.subs.repository.repos.submittables.support.SubmittablesAggregateSupport;

import java.util.List;

@Component
public class EgaDacPolicyRepositoryImpl implements SubmittableRepositoryCustom<EgaDacPolicy> {

    private SubmittablesAggregateSupport<EgaDacPolicy> aggregateSupport;

    public EgaDacPolicyRepositoryImpl(@Autowired MongoTemplate mongoTemplate) {
        this.aggregateSupport = new SubmittablesAggregateSupport<>(mongoTemplate, EgaDacPolicy.class);
    }

    @Override
    public Page<EgaDacPolicy> submittablesInTeam(String teamName, Pageable pageable) {
        return aggregateSupport.itemsByTeam(teamName, pageable);
    }

    @Override
    public Page<EgaDacPolicy> submittablesInTeams(List<String> teamNames, Pageable pageable) {
        return aggregateSupport.itemsByTeams(teamNames, pageable);
    }
}
