package Prueba_Backend_Accenture.aplication.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Product name is required")
        private String name;

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be >= 0")
        private Integer stock;

        @NotNull(message = "Branch ID is required")
        private Long branchId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateStockRequest {
        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be >= 0")
        private Integer stock;
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
        private Integer stock;
        private Long branchId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopStockResponse {
        private Long productId;
        private String productName;
        private Integer stock;
        private Long branchId;
        private String branchName;
    }
}