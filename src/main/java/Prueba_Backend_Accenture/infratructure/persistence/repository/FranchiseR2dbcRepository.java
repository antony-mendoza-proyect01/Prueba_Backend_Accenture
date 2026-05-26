package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.infratructure.persistence.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {
}