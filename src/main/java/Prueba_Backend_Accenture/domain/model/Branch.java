package Prueba_Backend_Accenture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    private Long id;
    private String name;
    private Long franchiseId;
    private List<Product> products;
}