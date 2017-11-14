package uk.ac.ebi.subs.repository.repos.submittables.support;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class SubmittablesAggregateSupport<T extends StoredSubmittable> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MongoTemplate mongoTemplate;
    private Class<T> clazz;

    public SubmittablesAggregateSupport(MongoTemplate mongoTemplate, Class<T> clazz) {
        this.mongoTemplate = mongoTemplate;
        this.clazz = clazz;
    }

    public Page<T> itemsByTeam(String teamName, Pageable pageable) {
        return itemsByTeams(Arrays.asList(teamName), pageable);
    }

    public Page<T> itemsByTeams(List<String> teamNames, Pageable pageable) {
        List<T> resultsList = getLimitedItemListByTeams(teamNames, pageable);
        long totalItemsCount = getTotalItemCountByTeams(teamNames);
        return new PageImpl<T>(resultsList, pageable, totalItemsCount);
    }

    private long getTotalItemCountByTeams(List<String> teamNames) {
        AggregationResults aggregationResults = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        teamMatchOperation(teamNames),
                        groupByAlias(),
                        group().count().as("count")
                ),
                clazz, clazz
        );

        Object results = aggregationResults.getRawResults().get("result");

        if (results != null && results instanceof BasicDBList) {
            BasicDBList resultsList = (BasicDBList) results;

            if (resultsList.isEmpty()) {
                return 0;
            }
            BasicDBObject countResult = (BasicDBObject) resultsList.get(0);

            if (countResult.containsField("count")) {
                return countResult.getLong("count");
            }

            return 0;


        }

        return -1;
    }

    private List<T> getLimitedItemListByTeams(List<String> teamNames, Pageable pageable) {
        final List<T> resultsList = new ArrayList<>();

        AggregationResults aggregationResults = mongoTemplate.aggregate(Aggregation.newAggregation(
                teamMatchOperation(teamNames),
                sort(pageable),
                groupByAliasWithFirstItem(),
                skip((long) pageable.getOffset()),
                limit((long) pageable.getPageSize()),
                replaceRoot("first")
        ), clazz, clazz);

        return aggregationResults.getMappedResults();
    }

    private GroupOperation groupByAliasWithFirstItem() {
        return group("alias", "team.name").first("$$ROOT").as("first");
    }

    private GroupOperation groupByAlias() {
        return group("alias", "team.name");
    }

    private SortOperation sort(Pageable pageable) {
        Sort sort;

        if (pageable.getSort() == null){
            sort = new Sort(Sort.Direction.DESC, "alias")
                    .and(new Sort(Sort.Direction.DESC, "createdDate"));
        }
        else {
            sort = pageable.getSort();
        }

        return Aggregation.sort(sort);
    }

    private MatchOperation teamMatchOperation(List<String> teamNames) {
        return match(where("team.name").in(teamNames));
    }

}
