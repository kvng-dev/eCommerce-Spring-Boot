package e_commerce.demo.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
