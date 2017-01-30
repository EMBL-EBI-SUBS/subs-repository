package uk.ac.ebi.subs.repository.model;

import uk.ac.ebi.subs.data.Submission;
import uk.ac.ebi.subs.data.submittable.Submittable;


public interface StoredSubmittable extends Submittable {
    Submission getSubmission();
    void setSubmission(Submission submission);
}

