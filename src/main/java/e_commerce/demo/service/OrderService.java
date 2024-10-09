package e_commerce.demo.service;

import e_commerce.demo.dto.CartDTO;
import e_commerce.demo.dto.OrderDTO;
import e_commerce.demo.exception.ResourceNotFoundException;
import e_commerce.demo.mapper.CartMapper;
import e_commerce.demo.mapper.OrderMapper;
import e_commerce.demo.model.*;
import e_commerce.demo.repository.OrderRepository;
import e_commerce.demo.repository.ProductRepository;
import e_commerce.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final EmailService emailService;
    private final CartService cartService;
    private final CartMapper cartMapper;

    @Transactional
    public OrderDTO createOrder(Long userId, String address, String phone) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isEmailConfirmation()) {
            throw new ResourceNotFoundException("Email is not confirmed");
        }
        // get cart
        CartDTO cartDto = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDto);

        if (cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phone);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        try {
            emailService.sendOrderConfirmation(savedOrder);
        } catch (Exception e) {
            logger.error("Failed to send email", e);
        }
        return orderMapper.toDTO(savedOrder);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                    if (product.getQuantity() == null) {
                        throw new ResourceNotFoundException("Product quantity is not set for product: " + product.getId());
                    }
                    if (product.getQuantity() < cartItem.getQuantity()) {
                        throw new ResourceNotFoundException("Not enough quantity for product: " + product.getId());
                    }
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(null, order, product,cartItem.getQuantity(), product.getPrice());
                }).collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders() {
        return orderMapper.toDTOs(orderRepository.findAll());
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderMapper.toDTOs(orderRepository.findByUserId(userId));
    }

    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }
}
