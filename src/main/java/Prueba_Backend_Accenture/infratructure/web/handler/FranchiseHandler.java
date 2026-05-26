package Prueba_Backend_Accenture.infratructure.web.handler;

import Prueba_Backend_Accenture.aplication.dto.FranchiseDto;
import Prueba_Backend_Accenture.aplication.usecase.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(FranchiseDto.CreateRequest.class)
                .flatMap(franchiseUseCase::createFranchise)
                .flatMap(response -> ServerResponse.status(201).bodyValue(response));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(FranchiseDto.UpdateNameRequest.class)
                .flatMap(req -> franchiseUseCase.updateFranchiseName(id, req))
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return franchiseUseCase.getFranchiseById(id)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(franchiseUseCase.getAllFranchises(), FranchiseDto.Response.class);
    }
}

