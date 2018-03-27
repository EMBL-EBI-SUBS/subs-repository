package uk.ac.ebi.subs.repository.repos;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.templates.Template;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TemplateRepositoryImpl implements TemplateRepositoryCustom {

    @NonNull
    private MongoTemplate mongoTemplate;

    public List<Template> findByTargetTypeAndTagsAllMatch(@Param("targetType") String targetType, @Param("tags") Collection tags) {
        log.info("custom template query impl");

        List<AggregationOperation> aggOps = Arrays.asList(
                Aggregation.match(Criteria.where("targetType").is(targetType).and("tags").all(tags)),
                Aggregation.sort(Sort.Direction.ASC, "name")
        );

        AggregationResults<Template> aggregationResults = mongoTemplate.aggregate(
                Aggregation.newAggregation(aggOps),
                Template.class,
                Template.class
        );

        return aggregationResults.getMappedResults();
    }


}
