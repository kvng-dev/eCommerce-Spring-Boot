package e_commerce.demo.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemDTO {

    private Long id;
    private Long productId;
    @Positive(message = "Quantity must be positive")
    private  Integer quantity;
}
