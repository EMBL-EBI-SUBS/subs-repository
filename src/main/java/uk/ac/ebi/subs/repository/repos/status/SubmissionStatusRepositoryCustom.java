package uk.ac.ebi.subs.repository.repos.status;

import java.util.List;
import java.util.Map;

public interface SubmissionStatusRepositoryCustom {

    Map<String,Integer> submissionStatusCountsByTeam(List<String> teamNames);
}
