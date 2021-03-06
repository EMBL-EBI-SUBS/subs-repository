package uk.ac.ebi.subs.repository.services;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import uk.ac.ebi.subs.data.status.ProcessingStatusEnum;
import uk.ac.ebi.subs.repository.model.ProcessingStatus;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;
import uk.ac.ebi.subs.repository.repos.status.ProcessingStatusRepository;
import uk.ac.ebi.subs.repository.repos.submittables.SubmittableRepository;
import uk.ac.ebi.subs.validator.data.ValidationResult;
import uk.ac.ebi.subs.validator.repository.ValidationResultRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(ArchiveProperties.class)
public class SubmittableHelperService {

    public SubmittableHelperService(ProcessingStatusRepository processingStatusRepository,
                                    ValidationResultRepository validationResultRepository,
                                    Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap,
                                    ArchiveProperties archiveProperties) {
        this.processingStatusRepository = processingStatusRepository;
        this.validationResultRepository = validationResultRepository;
        this.submittableRepositoryMap = submittableRepositoryMap;
        this.archiveProperties = archiveProperties;
    }

    private ProcessingStatusRepository processingStatusRepository;
    private ValidationResultRepository validationResultRepository;
    private Map<Class<? extends StoredSubmittable>, SubmittableRepository<? extends StoredSubmittable>> submittableRepositoryMap;
    private ArchiveProperties archiveProperties;

    public void setupNewSubmittable(StoredSubmittable submittable) {
        uuidAndTeamFromSubmissionSetUp(submittable);
        processingStatusAndValidationResultSetUpForSubmittable(submittable);
        fillInReferences(submittable);
    }

    public static void fillInReferences(StoredSubmittable submittable) {

        submittable.setReferences(
                submittable.refs()
                        .collect(Collectors.groupingBy(
                                ref -> ref.getClass().getSimpleName()
                                )
                        )
        );

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

    public void processingStatusAndValidationResultSetUpForSubmittable(StoredSubmittable submittable) {
        ValidationResult validationResult = createValidationResult(submittable);
        ProcessingStatus processingStatus = createProcessingStatus(submittable);

        submittable.setValidationResult(validationResult);
        submittable.setProcessingStatus(processingStatus);
        SubmittableRepository repository = submittableRepositoryMap.get(submittable.getClass());
        repository.save(submittable);

    }

    public void setTeamFromSubmission(StoredSubmittable submittable) {
        if (submittable.getSubmission() != null) {
            submittable.setTeam(submittable.getSubmission().getTeam());
        }
    }

    private ValidationResult createValidationResult(StoredSubmittable submittable) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setEntityUuid(submittable.getId());
        validationResult.setUuid(UUID.randomUUID().toString());

        validationResult.setEntityType(submittable.getClass().getCanonicalName()); // Must be the full qualified class name
        validationResult.setDataTypeId(submittable.getDataType().getId());
        validationResult.setSubmissionId(submittable.getSubmission().getId());
        return validationResultRepository.save(validationResult);
    }

    private ProcessingStatus createProcessingStatus(StoredSubmittable submittable) {
        ProcessingStatus processingStatus = ProcessingStatus.createForSubmittable(submittable);

        if (!archiveProperties.enabledDataTypeNames().contains(processingStatus.getDataType().getId())) {
            processingStatus.setStatus(ProcessingStatusEnum.ArchiveDisabled);
        }

        processingStatus.setId(UUID.randomUUID().toString());
        return processingStatusRepository.insert(processingStatus);
    }

}