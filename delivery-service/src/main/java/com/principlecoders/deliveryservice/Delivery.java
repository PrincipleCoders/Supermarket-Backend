// Delivery.java

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {
    private String id;
    private String userId;
    private Boolean markToDeliver;
    private Boolean isDelivered;
}

