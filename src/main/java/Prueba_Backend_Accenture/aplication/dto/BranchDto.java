package Prueba_Backend_Accenture.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BranchDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BranchCreateRequest")
    public static class CreateRequest {
        @NotBlank(message = "Branch name is required")
        private String name;

        @NotNull(message = "Franchise ID is required")
        private Long franchiseId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @Schema(name = "BranchUpdateNameRequest")
    public static class UpdateNameRequest {
        @NotBlank(message = "Name is required")
        private String name;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @Schema(name = "BranchResponse")
    public static class Response {
        private Long id;
        private String name;
        private Long franchiseId;
    }
}
