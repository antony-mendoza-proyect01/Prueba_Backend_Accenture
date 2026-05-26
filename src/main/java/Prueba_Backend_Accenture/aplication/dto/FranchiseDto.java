package Prueba_Backend_Accenture.aplication.dto;

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
    public static class CreateRequest {
        @NotBlank(message = "Franchise name is required")
        private String name;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateNameRequest {
        @NotBlank(message = "Name is required")
        private String name;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
    }
}
