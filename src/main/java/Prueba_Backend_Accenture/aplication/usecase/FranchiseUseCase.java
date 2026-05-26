package Prueba_Backend_Accenture.aplication.usecase;

import Prueba_Backend_Accenture.aplication.dto.FranchiseDto;
import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.service.FranchiseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseDomainService franchiseDomainService;

    public Mono<FranchiseDto.Response> createFranchise(FranchiseDto.CreateRequest request) {
        Franchise franchise = Franchise.builder()
                .name(request.getName())
                .build();
        return franchiseDomainService.create(franchise)
                .map(this::toResponse);
    }

    public Mono<FranchiseDto.Response> updateFranchiseName(Long id, FranchiseDto.UpdateNameRequest request) {
        return franchiseDomainService.updateName(id, request.getName())
                .map(this::toResponse);
    }

    public Mono<FranchiseDto.Response> getFranchiseById(Long id) {
        return franchiseDomainService.findById(id)
                .map(this::toResponse);
    }

    public Flux<FranchiseDto.Response> getAllFranchises() {
        return franchiseDomainService.findAll()
                .map(this::toResponse);
    }

    private FranchiseDto.Response toResponse(Franchise franchise) {
        return FranchiseDto.Response.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}

