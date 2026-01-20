package mg.tsiry.invetory_management_system.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tsiry Valisoa
 */
@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    private String description;
    private String imageUrl;
    private LocalDate expiryDate;
    private LocalDateTime updatedAt;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Transaction> transactions;

}
