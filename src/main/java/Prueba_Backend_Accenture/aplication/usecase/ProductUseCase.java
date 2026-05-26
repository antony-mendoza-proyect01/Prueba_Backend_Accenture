package Prueba_Backend_Accenture.aplication.usecase;

import Prueba_Backend_Accenture.aplication.dto.ProductDto;
import Prueba_Backend_Accenture.domain.model.Product;
import Prueba_Backend_Accenture.domain.service.BranchDomainService;
import Prueba_Backend_Accenture.domain.service.FranchiseDomainService;
import Prueba_Backend_Accenture.domain.service.ProductDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductDomainService productDomainService;
    private final BranchDomainService branchDomainService;
    private final FranchiseDomainService franchiseDomainService;

    public Mono<ProductDto.Response> createProduct(ProductDto.CreateRequest request) {
        return branchDomainService.findById(request.getBranchId())
                .flatMap(branch -> {
                    Product product = Product.builder()
                            .name(request.getName())
                            .stock(request.getStock())
                            .branchId(request.getBranchId())
                            .build();
                    return productDomainService.create(product);
                })
                .map(this::toResponse);
    }

    public Mono<Void> deleteProduct(Long id) {
        return productDomainService.delete(id);
    }

    public Mono<ProductDto.Response> updateStock(Long id, ProductDto.UpdateStockRequest request) {
        return productDomainService.updateStock(id, request.getStock())
                .map(this::toResponse);
    }

    public Mono<ProductDto.Response> updateName(Long id, ProductDto.UpdateNameRequest request) {
        return productDomainService.updateName(id, request.getName())
                .map(this::toResponse);
    }

    public Flux<ProductDto.TopStockResponse> getTopStockProductsByFranchise(Long franchiseId) {
        return franchiseDomainService.findById(franchiseId)
                .flatMapMany(franchise ->
                        branchDomainService.findByFranchiseId(franchiseId)
                                .flatMap(branch ->
                                        productDomainService.findTopStockByBranchId(branch.getId())
                                                .map(product -> ProductDto.TopStockResponse.builder()
                                                        .productId(product.getId())
                                                        .productName(product.getName())
                                                        .stock(product.getStock())
                                                        .branchId(branch.getId())
                                                        .branchName(branch.getName())
                                                        .build()
                                                )
                                )
                );
    }

    private ProductDto.Response toResponse(Product product) {
        return ProductDto.Response.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }
}
