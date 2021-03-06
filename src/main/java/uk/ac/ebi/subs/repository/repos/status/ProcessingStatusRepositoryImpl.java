package uk.ac.ebi.subs.repository.repos.status;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.DataType;
import uk.ac.ebi.subs.repository.model.ProcessingStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class ProcessingStatusRepositoryImpl implements ProcessingStatusRepositoryCustom {

    private MongoTemplate mongoTemplate;

    public ProcessingStatusRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<String, Integer> summariseSubmissionStatus(String submissionId) {
        Aggregation agg = Aggregation.newAggregation(
                submissionMatchOperation(submissionId),
                groupByStatus(),
                projectStatusCount()
        );

        AggregationResults<StatusSummary> aggregationResults = mongoTemplate.aggregate(
                agg, ProcessingStatus.class, StatusSummary.class
        );

        List<StatusSummary> statusSummaries = aggregationResults.getMappedResults();

        Map<String, Integer> statusCounts = new HashMap<>();

        for (StatusSummary statusSummary : statusSummaries) {
            statusCounts.put(statusSummary.getStatus(), statusSummary.getCount());
        }
        return statusCounts;
    }

    @Override
    public Map<String, Map<String, Integer>> summariseSubmissionStatusAndType(String submissionId) {
        Aggregation agg = Aggregation.newAggregation(
                submissionMatchOperation(submissionId),
                groupByTypeAndStatus(),
                projectTypeStatusCount()
        );

        AggregationResults<TypeStatusSummary> aggregationResults = mongoTemplate.aggregate(
                agg, ProcessingStatus.class, TypeStatusSummary.class
        );

        List<TypeStatusSummary> statusSummaries = aggregationResults.getMappedResults();

        Map<String, Map<String, Integer>> typeStatusCounts = new HashMap<>();

        for (TypeStatusSummary typeStatusSummary : statusSummaries) {

            if (!typeStatusCounts.containsKey(typeStatusSummary.getType())) {
                typeStatusCounts.put(typeStatusSummary.getType(), new HashMap<>());
            }

            typeStatusCounts
                    .get(typeStatusSummary.getType())
                    .put(typeStatusSummary.getStatus(), typeStatusSummary.getCount());

        }


        return typeStatusCounts;
    }

    @Override
    public Map<String, Set<String>> summariseSubmissionTypesWithSubmittableIds(String submissionId, Collection<String> relevantStatuses) {
        Aggregation agg =  Aggregation.newAggregation(
                submissionMatchOperation(submissionId),
                match(where("status").in(relevantStatuses)),
                group("submittableType").addToSet("submittableId").as("submittableIds"),
                project("submittableIds").and("_id").as("submittableType")
        );



        AggregationResults<TypeSubmittableIds> aggregationResults = mongoTemplate.aggregate(agg,ProcessingStatus.class,TypeSubmittableIds.class);

        Map<String,Set<String>> idsByType = new HashMap<>();

        for (TypeSubmittableIds typeIds : aggregationResults.getMappedResults()){
            idsByType.put(typeIds.getSubmittableType(),typeIds.getSubmittableIds());
        }

        return idsByType;
    }

    @Override
    public Map<DataType, Set<String>> summariseDataTypesWithSubmittableIds(String submissionId, Collection<String> relevantStatuses) {
        Aggregation agg =  Aggregation.newAggregation(
                submissionMatchOperation(submissionId),
                match(where("status").in(relevantStatuses)),
                group("dataType").addToSet("submittableId").as("submittableIds"),
                project("submittableIds").and("_id").as("dataType")
        );

        AggregationResults<DataTypeSubmittableIds> aggregationResults = mongoTemplate.aggregate(agg,ProcessingStatus.class, DataTypeSubmittableIds.class);

        Map<DataType,Set<String>> idsByType = new HashMap<>();

        for (DataTypeSubmittableIds typeIds : aggregationResults.getMappedResults()){
            idsByType.put(typeIds.getDataType(), typeIds.getSubmittableIds());
        }

        return idsByType;
    }

    private MatchOperation submissionMatchOperation(String submissionId) {
        return match(where("submissionId").is(submissionId));
    }

    private GroupOperation groupByStatus() {
        return group("status").count().as("count");
    }

    private ProjectionOperation projectStatusCount() {
        return project("count").and("_id").as("status");
    }

    private GroupOperation groupByTypeAndStatus() {
        return group("submittableType", "status").count().as("count");
    }

    private ProjectionOperation projectTypeStatusCount() {
        return project("count").and("_id.status").as("status").and("_id.submittableType").as("type");
    }


    public class StatusSummary {

        private String status;
        private int count;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public class TypeStatusSummary extends StatusSummary {
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class TypeSubmittableIds {
        private String submittableType;
        private Set<String> submittableIds;

        public String getSubmittableType() {
            return submittableType;
        }

        public void setSubmittableType(String submittableType) {
            this.submittableType = submittableType;
        }

        public Set<String> getSubmittableIds() {
            return submittableIds;
        }

        public void setSubmittableIds(Set<String> submittableIds) {
            this.submittableIds = submittableIds;
        }
    }

    public class DataTypeSubmittableIds {
        private DataType dataType;
        private Set<String> submittableIds;

        public DataType getDataType() {
            return dataType;
        }

        public void setDataType(DataType dataType) {
            this.dataType = dataType;
        }

        public Set<String> getSubmittableIds() {
            return submittableIds;
        }

        public void setSubmittableIds(Set<String> submittableIds) {
            this.submittableIds = submittableIds;
        }
    }
}
