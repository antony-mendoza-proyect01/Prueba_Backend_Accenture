package Prueba_Backend_Accenture.domain.repository;

import Prueba_Backend_Accenture.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Flux<Product> findByBranchId(Long branchId);
    Mono<Product> update(Product product);
    Mono<Void> deleteById(Long id);
    Mono<Boolean> existsById(Long id);
    Mono<Product> findTopStockByBranchId(Long branchId);
}