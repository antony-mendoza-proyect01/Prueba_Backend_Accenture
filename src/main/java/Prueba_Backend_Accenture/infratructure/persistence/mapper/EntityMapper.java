package Prueba_Backend_Accenture.infratructure.persistence.mapper;

import Prueba_Backend_Accenture.domain.model.Branch;
import Prueba_Backend_Accenture.domain.model.Franchise;
import Prueba_Backend_Accenture.domain.model.Product;
import Prueba_Backend_Accenture.infratructure.persistence.entity.BranchEntity;
import Prueba_Backend_Accenture.infratructure.persistence.entity.FranchiseEntity;
import Prueba_Backend_Accenture.infratructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public Franchise toDomain(FranchiseEntity entity) {
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public FranchiseEntity toEntity(Franchise domain) {
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }

    public Branch toDomain(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

    public BranchEntity toEntity(Branch domain) {
        return BranchEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .franchiseId(domain.getFranchiseId())
                .build();
    }

    public Product toDomain(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }

    public ProductEntity toEntity(Product domain) {
        return ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .stock(domain.getStock())
                .branchId(domain.getBranchId())
                .build();
    }
}

