package Prueba_Backend_Accenture.aplication.usecase;

import Prueba_Backend_Accenture.aplication.dto.BranchDto;
import Prueba_Backend_Accenture.domain.model.Branch;
import Prueba_Backend_Accenture.domain.service.BranchDomainService;
import Prueba_Backend_Accenture.domain.service.FranchiseDomainService;
import Prueba_Backend_Accenture.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchDomainService branchDomainService;
    private final FranchiseDomainService franchiseDomainService;

    public Mono<BranchDto.Response> createBranch(BranchDto.CreateRequest request) {
        return franchiseDomainService.findById(request.getFranchiseId())
                .switchIfEmpty(Mono.error(new NotFoundException("Franchise not found with id: " + request.getFranchiseId())))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .name(request.getName())
                            .franchiseId(request.getFranchiseId())
                            .build();
                    return branchDomainService.create(branch);
                })
                .map(this::toResponse);
    }

    public Mono<BranchDto.Response> updateBranchName(Long id, BranchDto.UpdateNameRequest request) {
        return branchDomainService.updateName(id, request.getName())
                .map(this::toResponse);
    }

    public Flux<BranchDto.Response> getByFranchiseId(Long franchiseId) {
        return branchDomainService.findByFranchiseId(franchiseId)
                .map(this::toResponse);
    }

    private BranchDto.Response toResponse(Branch branch) {
        return BranchDto.Response.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}
