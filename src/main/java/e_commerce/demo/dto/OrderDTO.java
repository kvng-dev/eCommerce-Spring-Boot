package e_commerce.demo.dto;

import e_commerce.demo.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private  Long id;
    private Long userId;
    @NotBlank(message = "Address is mandatory")
    private String address;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    private Order.OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> orderItems;
}
