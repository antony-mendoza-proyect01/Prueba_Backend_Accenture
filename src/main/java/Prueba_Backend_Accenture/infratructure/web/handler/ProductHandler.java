package Prueba_Backend_Accenture.infratructure.web.handler;

import Prueba_Backend_Accenture.aplication.dto.ProductDto;
import Prueba_Backend_Accenture.aplication.usecase.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ProductDto.CreateRequest.class)
                .flatMap(productUseCase::createProduct)
                .flatMap(response -> ServerResponse.status(201).bodyValue(response));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return productUseCase.deleteProduct(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(ProductDto.UpdateStockRequest.class)
                .flatMap(req -> productUseCase.updateStock(id, req))
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(ProductDto.UpdateNameRequest.class)
                .flatMap(req -> productUseCase.updateName(id, req))
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> getTopStockByFranchise(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
        return ServerResponse.ok()
                .body(productUseCase.getTopStockProductsByFranchise(franchiseId), ProductDto.TopStockResponse.class);
    }
}

