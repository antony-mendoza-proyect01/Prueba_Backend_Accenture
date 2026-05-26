package Prueba_Backend_Accenture.infratructure.web.controller;

import Prueba_Backend_Accenture.aplication.dto.BranchDto;
import Prueba_Backend_Accenture.aplication.dto.FranchiseDto;
import Prueba_Backend_Accenture.aplication.dto.ProductDto;
import Prueba_Backend_Accenture.aplication.usecase.BranchUseCase;
import Prueba_Backend_Accenture.aplication.usecase.FranchiseUseCase;
import Prueba_Backend_Accenture.aplication.usecase.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class FranchiseController {

    private final FranchiseUseCase franchiseUseCase;
    private final BranchUseCase branchUseCase;
    private final ProductUseCase productUseCase;

    public FranchiseController(FranchiseUseCase franchiseUseCase,
                               BranchUseCase branchUseCase,
                               ProductUseCase productUseCase) {
        this.franchiseUseCase = franchiseUseCase;
        this.branchUseCase = branchUseCase;
        this.productUseCase = productUseCase;
    }

    // ── Franquicias ──────────────────────────────────────────────────

    @Tag(name = "Franquicias")
    @Operation(summary = "Crear una nueva franquicia")
    @PostMapping("/franchises")
    public Mono<FranchiseDto.Response> createFranchise(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nombre de la franquicia",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = FranchiseDto.CreateRequest.class)))
            @RequestBody FranchiseDto.CreateRequest request) {
        return franchiseUseCase.createFranchise(request);
    }

    @Tag(name = "Franquicias")
    @Operation(summary = "Listar todas las franquicias")
    @GetMapping("/franchises")
    public Flux<FranchiseDto.Response> getAllFranchises() {
        return franchiseUseCase.getAllFranchises();
    }

    @Tag(name = "Franquicias")
    @Operation(summary = "Obtener franquicia por ID")
    @GetMapping("/franchises/{id}")
    public Mono<FranchiseDto.Response> getFranchiseById(@PathVariable Long id) {
        return franchiseUseCase.getFranchiseById(id);
    }

    @Tag(name = "Franquicias")
    @Operation(summary = "Actualizar nombre de franquicia")
    @PatchMapping("/franchises/{id}/name")
    public Mono<FranchiseDto.Response> updateFranchiseName(@PathVariable Long id,
                                                           @RequestBody FranchiseDto.UpdateNameRequest request) {
        return franchiseUseCase.updateFranchiseName(id, request);
    }

    // ── Sucursales ───────────────────────────────────────────────────

    @Tag(name = "Sucursales")
    @Operation(summary = "Crear una nueva sucursal")
    @PostMapping("/branches")
    public Mono<BranchDto.Response> createBranch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = BranchDto.CreateRequest.class)))
            @RequestBody BranchDto.CreateRequest request) {
        return branchUseCase.createBranch(request);
    }

    @Tag(name = "Sucursales")
    @Operation(summary = "Listar sucursales de una franquicia")
    @GetMapping("/franchises/{franchiseId}/branches")
    public Flux<BranchDto.Response> getBranchesByFranchise(@PathVariable Long franchiseId) {
        return branchUseCase.getByFranchiseId(franchiseId);
    }

    @Tag(name = "Sucursales")
    @Operation(summary = "Actualizar nombre de sucursal")
    @PatchMapping("/branches/{id}/name")
    public Mono<BranchDto.Response> updateBranchName(@PathVariable Long id,
                                                     @RequestBody BranchDto.UpdateNameRequest request) {
        return branchUseCase.updateBranchName(id, request);
    }

    // ── Productos ────────────────────────────────────────────────────

    @Tag(name = "Productos")
    @Operation(summary = "Crear un nuevo producto")
    @PostMapping("/products")
    public Mono<ProductDto.Response> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = ProductDto.CreateRequest.class)))
            @RequestBody ProductDto.CreateRequest request) {
        return productUseCase.createProduct(request);
    }

    @Tag(name = "Productos")
    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/products/{id}")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return productUseCase.deleteProduct(id);
    }

    @Tag(name = "Productos")
    @Operation(summary = "Modificar stock de un producto")
    @PatchMapping("/products/{id}/stock")
    public Mono<ProductDto.Response> updateStock(@PathVariable Long id,
                                                 @RequestBody ProductDto.UpdateStockRequest request) {
        return productUseCase.updateStock(id, request);
    }

    @Tag(name = "Productos")
    @Operation(summary = "Actualizar nombre de producto")
    @PatchMapping("/products/{id}/name")
    public Mono<ProductDto.Response> updateProductName(@PathVariable Long id,
                                                       @RequestBody ProductDto.UpdateNameRequest request) {
        return productUseCase.updateName(id, request);
    }

    @Tag(name = "Productos")
    @Operation(summary = "Producto con más stock por sucursal para una franquicia")
    @GetMapping("/franchises/{franchiseId}/top-stock-products")
    public Flux<ProductDto.TopStockResponse> getTopStockProducts(@PathVariable Long franchiseId) {
        return productUseCase.getTopStockProductsByFranchise(franchiseId);
    }
}