package Prueba_Backend_Accenture.infratructure.web.router;

import Prueba_Backend_Accenture.infratructure.web.handler.BranchHandler;
import Prueba_Backend_Accenture.infratructure.web.handler.FranchiseHandler;
import Prueba_Backend_Accenture.infratructure.web.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Profile("disabled")
@Configuration
public class ApiRouter {

    private static final String FRANCHISE_BASE = "/api/v1/franchises";
    private static final String BRANCH_BASE    = "/api/v1/branches";
    private static final String PRODUCT_BASE   = "/api/v1/products";

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions.route()
                .POST(FRANCHISE_BASE, handler::create)
                .GET(FRANCHISE_BASE, handler::getAll)
                .GET(FRANCHISE_BASE + "/{id}", handler::getById)
                .PATCH(FRANCHISE_BASE + "/{id}/name", handler::updateName)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions.route()
                .POST(BRANCH_BASE, handler::create)
                .GET(FRANCHISE_BASE + "/{franchiseId}/branches", handler::getByFranchise)
                .PATCH(BRANCH_BASE + "/{id}/name", handler::updateName)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                .POST(PRODUCT_BASE, handler::create)
                .DELETE(PRODUCT_BASE + "/{id}", handler::delete)
                .PATCH(PRODUCT_BASE + "/{id}/stock", handler::updateStock)
                .PATCH(PRODUCT_BASE + "/{id}/name", handler::updateName)
                .GET(FRANCHISE_BASE + "/{franchiseId}/top-stock-products", handler::getTopStockByFranchise)
                .build();
    }
}