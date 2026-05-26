package Prueba_Backend_Accenture.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FranchiseDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FranchiseCreateRequest")
    public static class CreateRequest {
        @NotBlank(message = "Franchise name is required")
        private String name;
    }
    @Schema(name = "FranchiseUpdateNameRequest")
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateNameRequest {
        @NotBlank(message = "Name is required")
        private String name;
    }

    @Schema(name = "FranchiseResponse")
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
    }
}
