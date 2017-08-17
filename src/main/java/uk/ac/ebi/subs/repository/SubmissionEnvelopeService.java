package uk.ac.ebi.subs.repository;

import uk.ac.ebi.subs.processing.SubmissionEnvelope;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;

import java.util.stream.Stream;

public interface SubmissionEnvelopeService {

    SubmissionEnvelope fetchOne(String submissionId);
    Stream<? extends StoredSubmittable> submissionContents(String submissionId);
}
