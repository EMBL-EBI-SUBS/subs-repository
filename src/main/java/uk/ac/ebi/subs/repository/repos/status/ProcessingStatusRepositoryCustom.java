package uk.ac.ebi.subs.repository.repos.status;

import uk.ac.ebi.subs.repository.model.DataType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface ProcessingStatusRepositoryCustom {

    Map<String, Integer> summariseSubmissionStatus(String submissionId);

    Map<String, Map<String, Integer>> summariseSubmissionStatusAndType(String submissionId);

    Map<String, Set<String>> summariseSubmissionTypesWithSubmittableIds(String submissionId, Collection<String> relevantStatuses);

    Map<DataType, Set<String>> summariseDataTypesWithSubmittableIds(String submissionId, Collection<String> relevantStatuses);
}
