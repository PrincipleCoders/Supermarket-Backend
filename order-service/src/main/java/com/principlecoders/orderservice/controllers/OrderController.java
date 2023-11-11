import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PutMapping("/updateMarkToDeliver/{orderId}")
    public ResponseEntity<Order> updateMarkToDeliver(
            @PathVariable String orderId,
            @RequestBody Map<String, Boolean> markStatus,
            @RequestHeader("Authorization") String token
    ) {
        // Validate token here if needed
        try {
            Order updatedOrder = orderService.updateMarkToDeliver(orderId, markStatus.get("markToDeliver"));
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


