package uk.ac.ebi.subs.repository.repos.status;

import lombok.Data;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.SubmissionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class SubmissionStatusRepositoryImpl implements SubmissionStatusRepositoryCustom {

    private MongoTemplate mongoTemplate;

    public SubmissionStatusRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<String, Integer> submissionStatusCountsByTeam(List<String> teamNames) {
        Aggregation agg = Aggregation.newAggregation(
                match(where("team.name").in(teamNames)),
                group("status").count().as("count"),
                project("count").and("_id").as("status")
        );

        AggregationResults<StatusSummary> aggregationResults = mongoTemplate.aggregate(
                agg, SubmissionStatus.class, StatusSummary.class
        );

        List<StatusSummary> statusSummaries = aggregationResults.getMappedResults();

        Map<String, Integer> statusCounts = new HashMap<>();

        for (StatusSummary statusSummary : statusSummaries) {
            statusCounts.put(statusSummary.getStatus(), statusSummary.getCount());
        }
        return statusCounts;
    }

    @Data
    private class StatusSummary {
        private String status;
        private int count;
    }

}
