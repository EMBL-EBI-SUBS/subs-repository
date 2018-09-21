package uk.ac.ebi.subs.repository.repos.submittables;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;

import java.util.List;


public interface SubmittableRepositoryCustom<T extends StoredSubmittable> {

    Page<T> submittablesInTeam(String teamName, Pageable pageable);

    Page<T> submittablesInTeams(List<String> teamNames, Pageable pageable);

    Page<T> findBySubmissionIdAndDataTypeIdWithErrors(String submissionId, String dataTypeId, Pageable pageable);

    Page<T> findBySubmissionIdAndDataTypeIdWithWarnings(String submissionId, String dataTypeId, Pageable pageable);
}
