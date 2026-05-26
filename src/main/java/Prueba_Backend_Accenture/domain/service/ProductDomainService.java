package Prueba_Backend_Accenture.domain.service;

import Prueba_Backend_Accenture.domain.model.Product;
import Prueba_Backend_Accenture.domain.repository.ProductRepository;
import Prueba_Backend_Accenture.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductDomainService {

    private final ProductRepository productRepository;

    public Mono<Product> create(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with id: " + id)));
    }

    public Flux<Product> findByBranchId(Long branchId) {
        return productRepository.findByBranchId(branchId);
    }

    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(p -> productRepository.deleteById(p.getId()));
    }

    public Mono<Product> updateStock(Long id, Integer stock) {
        return findById(id)
                .flatMap(product -> {
                    product.setStock(stock);
                    return productRepository.update(product);
                });
    }

    public Mono<Product> updateName(Long id, String newName) {
        return findById(id)
                .flatMap(product -> {
                    product.setName(newName);
                    return productRepository.update(product);
                });
    }

    public Mono<Product> findTopStockByBranchId(Long branchId) {
        return productRepository.findTopStockByBranchId(branchId);
    }
}

