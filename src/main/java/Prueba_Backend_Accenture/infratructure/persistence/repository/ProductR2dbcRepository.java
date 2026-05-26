package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.infratructure.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    Flux<ProductEntity> findByBranchId(Long branchId);

    @Query("SELECT * FROM product WHERE branch_id = :branchId ORDER BY stock DESC LIMIT 1")
    Mono<ProductEntity> findTopByBranchIdOrderByStockDesc(Long branchId);
}
