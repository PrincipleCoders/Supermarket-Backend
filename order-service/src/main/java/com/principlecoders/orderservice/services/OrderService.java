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
        }
        else {
            Map<String, Integer> productsQuantity = cart.getProductsQuantity();
            ProductDto productDto = getProductFromService(cartItemDto.getProductId());
            if (productDto.getQuantity() < cartItemDto.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Quantity of product is not enough");
            }

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
        if (cart!=null) {
            Map<String, Integer> productsQuantity = cart.getProductsQuantity();
            if (productsQuantity.containsKey(productId)) {
                productsQuantity.remove(productId);
                cart.setProductsQuantity(productsQuantity);
                cartRepository.save(cart);
                return ResponseEntity.status(HttpStatus.OK).body("Item removed from the cart");
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Item not found in the cart");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cart not found");
        }
    }


    public ResponseEntity<?> getRemainingOrders() {
        List<Order> remainingOrders = orderRepository.findAllByIsPackedIsFalse();
        if (remainingOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<RemainingOrderDto> remainingOrderDtos = new ArrayList<>();
        try {
            remainingOrders.forEach(order -> {
                UserDto userDto = getUserFromService(order.getUserId());

                List<ItemQuantity> itemQuantities = new ArrayList<>();
                order.getOrderProducts().forEach(orderProduct -> {
                    ProductDto productDto = getProductFromService(orderProduct.getProductId());
                    itemQuantities.add(ItemQuantity.builder()
                            .item(productDto.getName())
                            .quantity(orderProduct.getQuantity())
                            .build());
                });

                remainingOrderDtos.add(RemainingOrderDto.builder()
                        .id(order.getId())
                        .date(order.getDate())
                        .customer(userDto.getName())
                        .items(itemQuantities)
                        .isPacked(order.isPacked())
                        .build());
            });
            return ResponseEntity.ok(remainingOrderDtos);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
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
            return ResponseEntity.ok().body(isDeleted);
        }
    }

    public ResponseEntity<?> getAllOrdersOfUsers() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CustomerOrderDto> customerOrderDtos = new ArrayList<>();
        try {
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
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<?> checkout(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null || cart.getProductsQuantity().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<OrderProduct> orderProducts;
        try {
            orderProducts = new ArrayList<>();
            cart.getProductsQuantity().forEach((productId, quantity) -> {
                ProductDto productDto = getProductFromService(productId);
                if (productDto.getQuantity() < quantity) {
                    System.out.println("not enough quantity of product: "+productDto.getName());
                    throw new RuntimeException("not enough quantity of product: "+productDto.getName());
                }

                orderProducts.add(OrderProduct.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .price(productDto.getPrice())
                        .build());
            });

            orderProducts.forEach(orderProduct -> {
                ProductDto productDto = decreaseProductQuantityFromService(orderProduct.getProductId(), orderProduct.getQuantity());
                System.out.println("product quantity decreased: "+productDto);
                if (productDto == null) {
                    throw new RuntimeException("Error while decreasing product quantity");
                }
            });
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

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
                .uri(INVENTORY_URL + "product/" + productId + "/quantity/decrease/"+decrement)
                .header("api-key", INVENTORY_API_KEY)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}





