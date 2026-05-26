package Prueba_Backend_Accenture.infratructure.persistence.repository;

import Prueba_Backend_Accenture.domain.model.Product;
import Prueba_Backend_Accenture.domain.repository.ProductRepository;
import Prueba_Backend_Accenture.infratructure.persistence.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductR2dbcRepository r2dbcRepository;
    private final EntityMapper mapper;

    @Override
    public Mono<Product> save(Product product) {
        return r2dbcRepository.save(mapper.toEntity(product))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return r2dbcRepository.findByBranchId(branchId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Product> update(Product product) {
        return r2dbcRepository.save(mapper.toEntity(product))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return r2dbcRepository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return r2dbcRepository.existsById(id);
    }

    @Override
    public Mono<Product> findTopStockByBranchId(Long branchId) {
        return r2dbcRepository.findTopByBranchIdOrderByStockDesc(branchId)
                .map(mapper::toDomain);
    }
}

