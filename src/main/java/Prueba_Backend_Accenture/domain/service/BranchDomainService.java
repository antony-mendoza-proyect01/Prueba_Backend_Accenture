package Prueba_Backend_Accenture.domain.service;

import Prueba_Backend_Accenture.domain.model.Branch;
import Prueba_Backend_Accenture.domain.repository.BranchRepository;
import Prueba_Backend_Accenture.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchDomainService {

    private final BranchRepository branchRepository;

    public Mono<Branch> create(Branch branch) {
        return branchRepository.save(branch);
    }

    public Mono<Branch> findById(Long id) {
        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Branch not found with id: " + id)));
    }

    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId);
    }

    public Mono<Branch> updateName(Long id, String newName) {
        return findById(id)
                .flatMap(branch -> {
                    branch.setName(newName);
                    return branchRepository.update(branch);
                });
    }
}
