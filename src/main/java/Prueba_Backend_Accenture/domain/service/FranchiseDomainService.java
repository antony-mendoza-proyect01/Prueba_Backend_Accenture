package Prueba_Backend_Accenture.domain.service;

import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.repository.FranchiseRepository;
import Prueba_Backend_Accenture.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseDomainService {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> create(Franchise franchise) {
        return franchiseRepository.save(franchise);
    }

    public Mono<Franchise> findById(Long id) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found with id: " + id)));
    }

    public Flux<Franchise> findAll() {
        return franchiseRepository.findAll();
    }

    public Mono<Franchise> updateName(Long id, String newName) {
        return findById(id)
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.update(franchise);
                });
    }
}
