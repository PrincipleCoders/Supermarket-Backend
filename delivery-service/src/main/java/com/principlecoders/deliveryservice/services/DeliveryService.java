package com.principlecoders.deliveryservice.services;

import com.principlecoders.common.dto.*;
import com.principlecoders.deliveryservice.models.Delivery;
import com.principlecoders.deliveryservice.repositories.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.principlecoders.common.utils.ServiceApiKeys.*;
import static com.principlecoders.common.utils.ServiceUrls.*;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final WebClient webClient;

    public ResponseEntity<?> updateMarkToDeliverStatus(String deliveryId, boolean status) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setMarkToDeliver(status);
            return ResponseEntity.ok(deliveryRepository.save(delivery));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    public ResponseEntity<?> updateDelivered(String deliveryId, boolean isDelivered) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);

        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            delivery.setDelivered(isDelivered);
            return ResponseEntity.ok(deliveryRepository.save(delivery));
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    public ResponseEntity<?> createDelivery(String orderId) {
        Delivery newDelivery = deliveryRepository.save(Delivery.builder()
                .orderId(orderId)
                .markToDeliver(false)
                .isDelivered(false)
                .delivererId(null)
                .build());

        if (newDelivery.getId() != null) {
            return ResponseEntity.ok(newDelivery);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<?> getAllReadyToDeliverOrders() {
        List<Delivery> deliveryReadyOrders = deliveryRepository.findAllByMarkToDeliverIsFalse();
        if (deliveryReadyOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<CustomerOrderProductsDto> customerOrderProductsDtos = new ArrayList<>();
        try {
            deliveryReadyOrders.forEach(delivery -> {
                OrderDto orderDto = getOrderFromService(delivery.getOrderId());
                UserDto userDto = getUserFromService(orderDto.getUserId());

                List<ItemQuantity> itemQuantities = new ArrayList<>();
                AtomicInteger bill = new AtomicInteger();
                orderDto.getOrderProducts().forEach(orderProduct -> {
                    ProductDto productDto = getProductFromService(orderProduct.getProductId());
                    itemQuantities.add(ItemQuantity.builder()
                            .item(productDto.getName())
                            .quantity(orderProduct.getQuantity())
                            .build());
                    bill.addAndGet(productDto.getPrice() * orderProduct.getQuantity());
                });

                customerOrderProductsDtos.add(CustomerOrderProductsDto.builder()
                        .id(orderDto.getId())
                        .date(orderDto.getDate())
                        .bill(bill.get())
                        .items(itemQuantities)
                        .customer(userDto.getName())
                        .address(userDto.getAddress())
                        .telephone(userDto.getTelephone())
                        .isDelivered(delivery.isDelivered())
                        .markToDeliver(delivery.isMarkToDeliver())
                        .build());
            });
            return ResponseEntity.ok(customerOrderProductsDtos);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteDelivery(String orderId) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findByOrderId(orderId);

        if (optionalDelivery.isPresent()) {
            deliveryRepository.delete(optionalDelivery.get());
            return ResponseEntity.ok().body("Delivery deleted successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

    private OrderDto getOrderFromService(String userId) {
        return webClient.get()
                .uri(ORDER_URL + userId)
                .header("api-key", ORDER_API_KEY)
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block();
    }
}
