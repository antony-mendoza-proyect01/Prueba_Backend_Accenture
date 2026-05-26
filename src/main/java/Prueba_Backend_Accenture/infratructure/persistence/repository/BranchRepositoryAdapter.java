package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.domain.model.Branch;
import Prueba_Backend_Accenture.domain.repository.BranchRepository;
import Prueba_Backend_Accenture.infratructure.persistence.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

    private final BranchR2dbcRepository r2dbcRepository;
    private final EntityMapper mapper;

    @Override
    public Mono<Branch> save(Branch branch) {
        return r2dbcRepository.save(mapper.toEntity(branch))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return r2dbcRepository.findByFranchiseId(franchiseId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        return r2dbcRepository.save(mapper.toEntity(branch))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }
}
