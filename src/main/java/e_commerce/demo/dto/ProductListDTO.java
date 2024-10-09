package e_commerce.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductListDTO {

    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Desc is required")
    private String description;
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    @PositiveOrZero(message = "Quantity must be positive")
    private Integer quantity;
    private String image;
}
