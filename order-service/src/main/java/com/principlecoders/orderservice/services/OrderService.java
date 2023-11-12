package com.principlecoders.orderservice.services;

import com.principlecoders.common.dto.*;
import com.principlecoders.orderservice.models.Cart;
import com.principlecoders.orderservice.models.Order;
import com.principlecoders.orderservice.models.OrderProduct;
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
        if (cart == null || cart.getProductsQuantity().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CartProductsDto> cartProductsDtos = new ArrayList<>();
        cart.getProductsQuantity().forEach((productId, quantity) -> {
            ProductDto productDto = getProductFromService(productId);
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



    public ResponseEntity<?> addOrUpdateCart(CartItemDto cartItemDto) {
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
                productsQuantity.replace(cartItemDto.getProductId(), cartItemDto.getQuantity());
            }
            else {
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


    public ResponseEntity<?> getRemainingOrders() {
        List<Order> remainingOrders = orderRepository.findAllByPackedIsFalse();
        if (remainingOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.ok(getOrderDetailDtos(remainingOrders));
        }
    }

    public ResponseEntity<?> getOrderDetailsOfUser(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.ok(getOrderDetailDtos(orders));
        }
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

    public ResponseEntity<?> getAllOrdersOfUsers() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CustomerOrderDto> customerOrderDtos = new ArrayList<>();
        orders.forEach(order -> {
            OrderDetailsDto orderDetailsDto = getOrderDetailDtos(orders).get(0);
            UserDto userDto = getUserFromService(order.getUserId());

            customerOrderDtos.add(CustomerOrderDto.builder()
                    .id(order.getId())
                    .date(order.getDate())
                    .status(order.getStatus())
                    .total(orderDetailsDto.getTotal())
                    .items(orderDetailsDto.getItems())
                    .customer(userDto.getName())
                    .address(userDto.getAddress())
                    .telephone(userDto.getTelephone())
                    .build());
        });
        return ResponseEntity.ok(customerOrderDtos);
    }

    public ResponseEntity<?> checkout(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null || cart.getProductsQuantity().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<OrderProduct> orderProducts = new ArrayList<>();
        cart.getProductsQuantity().forEach((productId, quantity) -> {
            ProductDto productDto = getProductFromService(productId);
            orderProducts.add(OrderProduct.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .price(productDto.getPrice())
                    .build());

            decreaseProductQuantityFromService(productId, quantity);
        });

        Order newOrder = orderRepository.save(Order.builder()
                .userId(userId)
                .orderProducts(orderProducts)
                .date(new Date())
                .status("processing")
                .isPacked(false)
                .build());

        if (newOrder.getId() != null) {
            cartRepository.delete(cart);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    private List<OrderDetailsDto> getOrderDetailDtos(List<Order> orders) {
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
        return orderDetailsDtos;
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


    private ProductDto decreaseProductQuantityFromService(String productId, int decrement) {
        return webClient.put()
                .uri(INVENTORY_URL + "product/" + productId + "/quantity/decrease")
                .body(decrement, Integer.class)
                .header("api-key", INVENTORY_API_KEY)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}





