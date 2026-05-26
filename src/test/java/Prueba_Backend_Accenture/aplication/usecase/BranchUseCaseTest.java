package Prueba_Backend_Accenture.aplication.usecase;

import Prueba_Backend_Accenture.aplication.dto.BranchDto;
import Prueba_Backend_Accenture.domain.model.Branch;
import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.service.BranchDomainService;
import Prueba_Backend_Accenture.domain.service.FranchiseDomainService;
import Prueba_Backend_Accenture.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @Mock
    private BranchDomainService branchDomainService;

    @Mock
    private FranchiseDomainService franchiseDomainService;

    @InjectMocks
    private BranchUseCase branchUseCase;

    private Branch sampleBranch;
    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        sampleBranch = Branch.builder().id(1L).name("Main Branch").franchiseId(1L).build();
        sampleFranchise = Franchise.builder().id(1L).name("Franchise X").build();
    }

    @Test
    @DisplayName("Should create branch when franchise exists")
    void shouldCreateBranch() {
        when(franchiseDomainService.findById(1L)).thenReturn(Mono.just(sampleFranchise));
        when(branchDomainService.create(any(Branch.class))).thenReturn(Mono.just(sampleBranch));

        BranchDto.CreateRequest request = new BranchDto.CreateRequest("Main Branch", 1L);

        StepVerifier.create(branchUseCase.createBranch(request))
                .expectNextMatches(r -> r.getName().equals("Main Branch") && r.getFranchiseId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when franchise not found")
    void shouldFailWhenFranchiseNotFound() {
        when(franchiseDomainService.findById(anyLong()))
                .thenReturn(Mono.error(new NotFoundException("Franchise not found with id: 99")));

        BranchDto.CreateRequest request = new BranchDto.CreateRequest("Branch", 99L);

        StepVerifier.create(branchUseCase.createBranch(request))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update branch name")
    void shouldUpdateBranchName() {
        Branch updated = Branch.builder().id(1L).name("Updated Branch").franchiseId(1L).build();
        when(branchDomainService.updateName(1L, "Updated Branch")).thenReturn(Mono.just(updated));

        BranchDto.UpdateNameRequest request = new BranchDto.UpdateNameRequest("Updated Branch");

        StepVerifier.create(branchUseCase.updateBranchName(1L, request))
                .expectNextMatches(r -> r.getName().equals("Updated Branch"))
                .verifyComplete();
    }
}
