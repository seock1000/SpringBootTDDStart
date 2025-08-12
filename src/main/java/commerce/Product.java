package commerce;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "sellerId"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    private UUID sellerId;

    private String name;
    private String imageUri;
    private String description;
    private BigDecimal priceAmount;
    private int stockQuantity;
    private LocalDateTime registeredTimeUtc;
}
