package e_commerce.demo.service;

import e_commerce.demo.dto.CartDTO;
import e_commerce.demo.exception.InsufficientStockException;
import e_commerce.demo.exception.ResourceNotFoundException;
import e_commerce.demo.mapper.CartMapper;
import e_commerce.demo.model.Cart;
import e_commerce.demo.model.CartItem;
import e_commerce.demo.model.Product;
import e_commerce.demo.model.User;
import e_commerce.demo.repository.CartRepository;
import e_commerce.demo.repository.ProductRepository;
import e_commerce.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDTO addToCart(Long productId, Long userId, Integer quantity) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough quantity");
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(null, user, new ArrayList<>()));
        Optional<CartItem> existingCart = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCart.isPresent()) {
            CartItem cartItem = existingCart.get();
            cartItem.setQuantity(cartItem.getQuantity()+quantity);        } else {
          CartItem cartItem = new CartItem(null, quantity, cart, product);
          cart.getItems().add(cartItem);
        }

        Cart saved = cartRepository.save(cart);
        return cartMapper.toDTO(saved);
    }

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cartMapper.toDTO(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
