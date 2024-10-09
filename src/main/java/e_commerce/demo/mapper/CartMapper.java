package e_commerce.demo.mapper;

import e_commerce.demo.dto.CartDTO;
import e_commerce.demo.dto.CartItemDTO;
import e_commerce.demo.model.Cart;
import e_commerce.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
   CartDTO toDTO(Cart cart);
    @Mapping(target = "user.id", source = "userId")
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target = "productId", source = "product.id")
    CartItemDTO toDTO(CartItem cartItem);
    @Mapping(target = "product.id", source = "productId")
    CartItem toEntity(CartItemDTO cartItemDTO);
}
