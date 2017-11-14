package uk.ac.ebi.subs.repository.repos.submittables;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.Protocol;
import uk.ac.ebi.subs.repository.repos.submittables.support.SubmittablesAggregateSupport;

import java.util.List;

@Component
public class ProtocolRepositoryImpl implements SubmittableRepositoryCustom<Protocol> {

    private SubmittablesAggregateSupport<Protocol> aggregateSupport;

    public ProtocolRepositoryImpl(MongoTemplate mongoTemplate) {
        this.aggregateSupport = new SubmittablesAggregateSupport<>(mongoTemplate, Protocol.class);
    }

    @Override
    public Page<Protocol> submittablesInTeam(String teamName, Pageable pageable) {
        return aggregateSupport.itemsByTeam(teamName, pageable);
    }

    @Override
    public Page<Protocol> submittablesInTeams(List<String> teamNames, Pageable pageable) {
        return aggregateSupport.itemsByTeams(teamNames, pageable);
    }

}
