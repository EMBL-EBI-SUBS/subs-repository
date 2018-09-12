package uk.ac.ebi.subs.repository.services;

import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.repository.model.ProcessingStatus;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;
import uk.ac.ebi.subs.repository.repos.status.ProcessingStatusRepository;
import uk.ac.ebi.subs.repository.repos.submittables.SubmittableRepository;
import uk.ac.ebi.subs.validator.data.ValidationResult;
import uk.ac.ebi.subs.validator.repository.ValidationResultRepository;

import java.util.Map;
import java.util.UUID;

@Component
public class SubmittableHelperService {

    public SubmittableHelperService(ProcessingStatusRepository processingStatusRepository, ValidationResultRepository validationResultRepository,
                                    Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap) {
        this.processingStatusRepository = processingStatusRepository;
        this.validationResultRepository = validationResultRepository;
        this.submittableRepositoryMap = submittableRepositoryMap;
    }

    private ProcessingStatusRepository processingStatusRepository;
    private ValidationResultRepository validationResultRepository;
    private Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap;

    public void setupNewSubmittable(StoredSubmittable submittable) {
        uuidAndTeamFromSubmissionSetUp(submittable);
        processingStatusAndValidationResultSetUp(submittable);
    }

    public void uuidAndTeamFromSubmissionSetUp(StoredSubmittable submittable) {
        submittable.setId(UUID.randomUUID().toString());

        // Spring Data Rest does not validate until after the 'Before' events have been handled, so we can't rely on having
        // a submission. If there's no submission, the submittable won't be valid
        if (submittable.getSubmission() == null) {
            return;
        }
        setTeamFromSubmission(submittable);
    }

    public void processingStatusAndValidationResultSetUp(StoredSubmittable submittable) {
        createValidationResult(submittable);
        createProcessingStatus(submittable);
    }

    public void setTeamFromSubmission(StoredSubmittable submittable) {
        if (submittable.getSubmission() != null) {
            submittable.setTeam(submittable.getSubmission().getTeam());
        }
    }

    private void createValidationResult(StoredSubmittable submittable) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setEntityUuid(submittable.getId());
        validationResult.setUuid(UUID.randomUUID().toString());

        validationResult.setEntityType(submittable.getClass().getCanonicalName()); // Must be the full qualified class name
        validationResult.setDataTypeId(submittable.getDataType().getId());
        validationResult.setSubmissionId(submittable.getSubmission().getId());
        validationResultRepository.save(validationResult);

        submittable.setValidationResult(validationResult);

        SubmittableRepository repository = submittableRepositoryMap.get(submittable.getClass());
        repository.save(submittable);
    }

    private void createProcessingStatus(StoredSubmittable submittable) {
        ProcessingStatus processingStatus = ProcessingStatus.createForSubmittable(submittable);
        processingStatus.setId(UUID.randomUUID().toString());
        processingStatusRepository.insert(processingStatus);

        SubmittableRepository repository = submittableRepositoryMap.get(submittable.getClass());
        repository.save(submittable);
    }

}