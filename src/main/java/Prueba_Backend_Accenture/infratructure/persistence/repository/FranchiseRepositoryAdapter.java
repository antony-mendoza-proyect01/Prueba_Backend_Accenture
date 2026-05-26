package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.repository.FranchiseRepository;
import Prueba_Backend_Accenture.infratructure.persistence.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseR2dbcRepository r2dbcRepository;
    private final EntityMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return r2dbcRepository.save(mapper.toEntity(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return r2dbcRepository.save(mapper.toEntity(franchise))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }
}
