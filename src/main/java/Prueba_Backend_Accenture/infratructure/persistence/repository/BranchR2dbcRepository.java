package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.infratructure.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);
}
