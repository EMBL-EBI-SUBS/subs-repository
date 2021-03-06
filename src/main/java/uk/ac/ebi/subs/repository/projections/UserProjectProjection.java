package uk.ac.ebi.subs.repository.projections;

import org.springframework.data.rest.core.config.Projection;
import uk.ac.ebi.subs.repository.model.Project;

import java.time.LocalDate;

@Projection(name = "userProjectProjection",types = {Project.class })
public interface UserProjectProjection extends SubmittableWithStatus {
    LocalDate getReleaseDate();
}
