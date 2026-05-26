package Prueba_Backend_Accenture.aplication.usecase;

import Prueba_Backend_Accenture.aplication.dto.ProductDto;
import Prueba_Backend_Accenture.domain.model.Branch;
import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.model.Product;
import Prueba_Backend_Accenture.domain.service.BranchDomainService;
import Prueba_Backend_Accenture.domain.service.FranchiseDomainService;
import Prueba_Backend_Accenture.domain.service.ProductDomainService;
import Prueba_Backend_Accenture.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductDomainService productDomainService;

    @Mock
    private BranchDomainService branchDomainService;

    @Mock
    private FranchiseDomainService franchiseDomainService;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Product sampleProduct;
    private Branch sampleBranch;
    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder().id(1L).name("Product A").stock(100).branchId(1L).build();
        sampleBranch  = Branch.builder().id(1L).name("Branch Central").franchiseId(1L).build();
        sampleFranchise = Franchise.builder().id(1L).name("Franchise X").build();
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProduct() {
        when(branchDomainService.findById(1L)).thenReturn(Mono.just(sampleBranch));
        when(productDomainService.create(any(Product.class))).thenReturn(Mono.just(sampleProduct));

        ProductDto.CreateRequest request = new ProductDto.CreateRequest("Product A", 100, 1L);

        StepVerifier.create(productUseCase.createProduct(request))
                .expectNextMatches(r -> r.getName().equals("Product A") && r.getStock().equals(100))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail to create product when branch not found")
    void shouldFailCreateProductWhenBranchMissing() {
        when(branchDomainService.findById(anyLong()))
                .thenReturn(Mono.error(new NotFoundException("Branch not found with id: 99")));

        ProductDto.CreateRequest request = new ProductDto.CreateRequest("Product A", 50, 99L);

        StepVerifier.create(productUseCase.createProduct(request))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() {
        Product updated = Product.builder().id(1L).name("Product A").stock(200).branchId(1L).build();
        when(productDomainService.updateStock(1L, 200)).thenReturn(Mono.just(updated));

        ProductDto.UpdateStockRequest request = new ProductDto.UpdateStockRequest(200);

        StepVerifier.create(productUseCase.updateStock(1L, request))
                .expectNextMatches(r -> r.getStock().equals(200))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProduct() {
        when(productDomainService.delete(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProduct(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get top stock products per branch for a franchise")
    void shouldGetTopStockProducts() {
        Branch branch2 = Branch.builder().id(2L).name("Branch North").franchiseId(1L).build();
        Product topProduct2 = Product.builder().id(2L).name("Product B").stock(500).branchId(2L).build();

        when(franchiseDomainService.findById(1L)).thenReturn(Mono.just(sampleFranchise));
        when(branchDomainService.findByFranchiseId(1L)).thenReturn(Flux.just(sampleBranch, branch2));
        when(productDomainService.findTopStockByBranchId(1L)).thenReturn(Mono.just(sampleProduct));
        when(productDomainService.findTopStockByBranchId(2L)).thenReturn(Mono.just(topProduct2));

        StepVerifier.create(productUseCase.getTopStockProductsByFranchise(1L))
                .expectNextCount(2)
                .verifyComplete();
    }
}

