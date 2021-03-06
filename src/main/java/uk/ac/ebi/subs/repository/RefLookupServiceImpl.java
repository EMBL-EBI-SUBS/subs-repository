package uk.ac.ebi.subs.repository;

import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.data.component.AbstractSubsRef;
import uk.ac.ebi.subs.repository.model.StoredSubmittable;
import uk.ac.ebi.subs.repository.repos.submittables.SubmittableRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RefLookupServiceImpl implements RefLookupService {

    private Map<Class<? extends AbstractSubsRef>, SubmittableRepository<? extends StoredSubmittable>> referenceRepositoryMap;

    public RefLookupServiceImpl(Map<Class<? extends AbstractSubsRef>, SubmittableRepository<? extends StoredSubmittable>> referenceRepositoryMap) {
        this.referenceRepositoryMap = referenceRepositoryMap;
    }

    @Override
    public <T extends AbstractSubsRef> StoredSubmittable lookupRef(T ref) {
        SubmittableRepository repo = referenceRepositoryMap.get(ref.getClass());

        if (repo == null) return null;

        if(ref.isAccessioned()) {
            return repo.findFirstByAccessionOrderByCreatedDateDesc(ref.getAccession());
        } else {
            return repo.findFirstByTeamNameAndAliasOrderByCreatedDateDesc(ref.getTeam(), ref.getAlias());
        }
    }

    @Override
    public <T extends AbstractSubsRef> Set<? extends StoredSubmittable> lookupRefs(Collection<T> refs) {
        return refs.stream().map(this::lookupRef).collect(Collectors.toSet());
    }

}
