package uk.ac.ebi.subs.repository.repos.submittables;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;

import java.util.List;


public interface SubmittableRepositoryCustom<T extends StoredSubmittable> {

    Page<T> submittablesInTeam(String teamName, Pageable pageable);

    Page<T> submittablesInTeams(List<String> teamNames, Pageable pageable);
}
