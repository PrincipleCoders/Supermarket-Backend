package com.principlecoders.orderservice.services;

import com.principlecoders.common.dto.CartItemDto;
import com.principlecoders.common.dto.CartProductsDto;
import com.principlecoders.common.dto.ProductDto;
import com.principlecoders.common.dto.UserRoleDto;
import com.principlecoders.orderservice.OrderServiceApplication;
import com.principlecoders.orderservice.models.Cart;
import com.principlecoders.orderservice.models.Order;
import com.principlecoders.orderservice.models.OrderLineItems;
import com.principlecoders.orderservice.repositories.CartRepository;
import com.principlecoders.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static com.principlecoders.common.utils.ServiceUrls.INVENTORY_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public ResponseEntity<?> getCartItemsOfUser(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        List<CartProductsDto> cartProductsDtos = new ArrayList<>();
        cart.getProductsQuantity().forEach((productId, quantity) -> {
            ProductDto productDto = webClient.get()
                    .uri(INVENTORY_URL + "product/" + productId)
                    .retrieve()
                    .bodyToMono(ProductDto.class)
                    .block();
            cartProductsDtos.add(CartProductsDto.builder()
                    .price(productDto.getPrice())
                    .productId(productId)
                    .name(productDto.getName())
                    .image(productDto.getImage())
                    .quantity(quantity)
                    .build());
        });
        return ResponseEntity.ok(cartProductsDtos);
    }

    public ResponseEntity<?> addToCart(CartItemDto cartItemDto) {
        Cart cart = cartRepository.findByUserId(cartItemDto.getUserId());
        if (cart == null) {
            Map<String, Integer> productsQuantity = new HashMap<>();
            productsQuantity.put(cartItemDto.getProductId(), cartItemDto.getQuantity());
            cart = Cart.builder()
                    .userId(cartItemDto.getUserId())
                    .productsQuantity(productsQuantity)
                    .build();
            Cart newCart = cartRepository.save(cart);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newCart);
        }
        else {
            Map<String, Integer> productsQuantity = cart.getProductsQuantity();
            if (productsQuantity.containsKey(cartItemDto.getProductId())) {
                productsQuantity.put(cartItemDto.getProductId(),
                        productsQuantity.get(cartItemDto.getProductId()) + cartItemDto.getQuantity());
            }
            else {
                productsQuantity.put(cartItemDto.getProductId(), cartItemDto.getQuantity());
            }
            cart.setProductsQuantity(productsQuantity);
            Cart updatedCart = cartRepository.save(cart);
            return ResponseEntity.ok(updatedCart);
        }
    }


    //Get list of orders customer made
    public  List<Order> getAllOrdersByCustomers(){
        return   orderRepository.findAll();
    }

//    public void createOrder(Order orderRequest){
//        Order order=Order.builder()
//                .id(orderRequest.getId())
//                .orderNumber(orderRequest.getOrderNumber())
////                .totalAmount(orderRequest.getTotalAmount())
//                .build();
//
//        orderRepository.save(order);
//        log.info ( "orderCreated");
//    }


    //plcae Order by customer
    public  void placeOrder(Order orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems>orderLineItems=orderRequest.getOrderLineItemsList().stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
    }

    private  OrderLineItems mapToDto(OrderLineItems orderLineItems){
        OrderLineItems orderLineItems1=new OrderLineItems();
        orderLineItems1.setPrice(orderLineItems.getPrice());
        orderLineItems1.setQauntity(orderLineItems.getQauntity());
        orderLineItems1.setSkuCode(orderLineItems.getSkuCode());

        return  orderLineItems1;
    }

    //Get some  customer details for combining order details
    public void getUser(UserRoleDto userRoleDto){
        UserRoleDto user=new UserRoleDto();
        user.getUserId();




    }
}
