package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.domain.model.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Flux<Franchise> findAll();
    Mono<Franchise> update(Franchise franchise);
    Mono<Boolean> existsById(Long id);
}
