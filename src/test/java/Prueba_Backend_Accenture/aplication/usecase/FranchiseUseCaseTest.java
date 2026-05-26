package Prueba_Backend_Accenture.aplication.usecase;

import Prueba_Backend_Accenture.aplication.dto.FranchiseDto;
import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.service.FranchiseDomainService;
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
class FranchiseUseCaseTest {

    @Mock
    private FranchiseDomainService franchiseDomainService;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    private Franchise sampleFranchise;

    @BeforeEach
    void setUp() {
        sampleFranchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
    }

    @Test
    @DisplayName("Should create franchise successfully")
    void shouldCreateFranchise() {
        when(franchiseDomainService.create(any(Franchise.class)))
                .thenReturn(Mono.just(sampleFranchise));

        FranchiseDto.CreateRequest request = new FranchiseDto.CreateRequest("Test Franchise");

        StepVerifier.create(franchiseUseCase.createFranchise(request))
                .expectNextMatches(r -> r.getId().equals(1L) && r.getName().equals("Test Franchise"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return franchise by id")
    void shouldGetFranchiseById() {
        when(franchiseDomainService.findById(1L))
                .thenReturn(Mono.just(sampleFranchise));

        StepVerifier.create(franchiseUseCase.getFranchiseById(1L))
                .expectNextMatches(r -> r.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw NotFoundException when franchise not found")
    void shouldThrowNotFoundWhenFranchiseMissing() {
        when(franchiseDomainService.findById(anyLong()))
                .thenReturn(Mono.error(new NotFoundException("Franchise not found with id: 99")));

        StepVerifier.create(franchiseUseCase.getFranchiseById(99L))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should update franchise name")
    void shouldUpdateFranchiseName() {
        Franchise updated = Franchise.builder().id(1L).name("New Name").build();
        when(franchiseDomainService.updateName(1L, "New Name"))
                .thenReturn(Mono.just(updated));

        FranchiseDto.UpdateNameRequest request = new FranchiseDto.UpdateNameRequest("New Name");

        StepVerifier.create(franchiseUseCase.updateFranchiseName(1L, request))
                .expectNextMatches(r -> r.getName().equals("New Name"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return all franchises")
    void shouldGetAllFranchises() {
        Franchise f2 = Franchise.builder().id(2L).name("Second Franchise").build();
        when(franchiseDomainService.findAll()).thenReturn(Flux.just(sampleFranchise, f2));

        StepVerifier.create(franchiseUseCase.getAllFranchises())
                .expectNextCount(2)
                .verifyComplete();
    }
}
