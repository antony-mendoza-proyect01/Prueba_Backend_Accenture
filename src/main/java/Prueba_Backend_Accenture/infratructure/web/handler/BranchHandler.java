package Prueba_Backend_Accenture.infratructure.web.handler;

import Prueba_Backend_Accenture.aplication.dto.BranchDto;
import Prueba_Backend_Accenture.aplication.usecase.BranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(BranchDto.CreateRequest.class)
                .flatMap(branchUseCase::createBranch)
                .flatMap(response -> ServerResponse.status(201).bodyValue(response));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(BranchDto.UpdateNameRequest.class)
                .flatMap(req -> branchUseCase.updateBranchName(id, req))
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> getByFranchise(ServerRequest request) {
        Long franchiseId = Long.parseLong(request.pathVariable("franchiseId"));
        return ServerResponse.ok().body(branchUseCase.getByFranchiseId(franchiseId), BranchDto.Response.class);
    }
}
