package com.principlecoders.orderservice.services;

import com.principlecoders.common.dto.*;
import com.principlecoders.orderservice.models.Cart;
import com.principlecoders.orderservice.models.Order;
import com.principlecoders.orderservice.repositories.CartRepository;
import com.principlecoders.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static com.principlecoders.common.utils.ServiceApiKeys.*;
import static com.principlecoders.common.utils.ServiceUrls.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final WebClient webClient;
    private final OrderRepository orderRepository;

    public ResponseEntity<?> getCartItemsOfUser(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        List<CartProductsDto> cartProductsDtos = new ArrayList<>();
        cart.getProductsQuantity().forEach((productId, quantity) -> {
            ProductDto productDto = webClient.get()
                    .uri(INVENTORY_URL + "product/" + productId)
                    .header("api-key", INVENTORY_API_KEY)
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
        } else {
            Map<String, Integer> productsQuantity = cart.getProductsQuantity();
            if (productsQuantity.containsKey(cartItemDto.getProductId())) {
                productsQuantity.put(cartItemDto.getProductId(),
                        productsQuantity.get(cartItemDto.getProductId()) + cartItemDto.getQuantity());
            } else {
                productsQuantity.put(cartItemDto.getProductId(), cartItemDto.getQuantity());
            }
            cart.setProductsQuantity(productsQuantity);
            Cart updatedCart = cartRepository.save(cart);
            return ResponseEntity.ok(updatedCart);
        }
    }

    public ResponseEntity<?> deleteCartItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart.getId()!=null) {
            Map<String, Integer> productsQuantity = cart.getProductsQuantity();
            if (productsQuantity.containsKey(productId)) {
                productsQuantity.remove(productId);
                cart.setProductsQuantity(productsQuantity);
                cartRepository.save(cart);
                return ResponseEntity.ok("Item deleted from the cart");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in the cart");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("cart not found");
        }
    }

    
    public ResponseEntity<?> getOrderDetailsOfUser(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
        orders.forEach(order -> {
            int total = order.getOrderProducts().stream()
                    .mapToInt(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity())
                    .sum();

            OrderDetailsDto orderDetailsDto = OrderDetailsDto.builder()
                    .id(order.getId())
                    .date(order.getDate())
                    .status(order.getStatus())
                    .items(order.getOrderProducts().size())
                    .total(total)
                    .build();

            orderDetailsDtos.add(orderDetailsDto);
        });
        return ResponseEntity.ok(orderDetailsDtos);
    }

    public ResponseEntity<?> getRemainingOrders() {
        List<Order> remainingOrders = orderRepository.findAllByPackedIsFalse();

        if (remainingOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            List<RemainingOrderDto> remainingOrderDtos = new ArrayList<>();
            remainingOrders.forEach(order -> {
                String customer = getUserFromService(order.getUserId()).getName();
                List<ItemQuantity> items = new ArrayList<>();

                order.getOrderProducts().forEach(orderProduct -> {
                    String productName = getProductFromService(orderProduct.getProductId()).getName();
                    items.add(ItemQuantity.builder()
                            .item(productName)
                            .quantity(orderProduct.getQuantity())
                            .build());
                });

                remainingOrderDtos.add(RemainingOrderDto.builder()
                        .id(order.getId())
                        .date(order.getDate())
                        .customer(customer)
                        .items(items)
                        .isPacked(order.isPacked())
                        .build());
            });
            return ResponseEntity.ok(remainingOrderDtos);
        }
    }






    private ProductDto getProductFromService(String productId) {
        return webClient.get()
                .uri(INVENTORY_URL + "product/" + productId)
                .header("api-key", INVENTORY_API_KEY)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }

    private UserDto getUserFromService(String userId) {
        return webClient.get()
                .uri(USER_URL + userId)
                .header("api-key", USER_API_KEY)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public ResponseEntity<?> updateOrderStatus(String orderId, boolean isPacked) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        order.setPacked(isPacked);
        Order newOrder = orderRepository.save(order);

        if (isPacked) {
            DeliveryDto deliveryDto = makeDeliveryRecordFromService(orderId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(newOrder+" "+deliveryDto);
        } else {
            boolean isDeleted = deleteDeliveryRecordFromService(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(isDeleted);
        }
    }

    private DeliveryDto makeDeliveryRecordFromService(String orderId) {
        return webClient.post()
                .uri(DELIVERY_URL + "order/" + orderId)
                .header("api-key", DELIVERY_API_KEY)
                .retrieve()
                .bodyToMono(DeliveryDto.class)
                .block();
    }

    private Boolean deleteDeliveryRecordFromService(String orderId) {
        return webClient.delete()
                .uri(DELIVERY_URL + "order/" + orderId)
                .header("api-key", DELIVERY_API_KEY)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}





