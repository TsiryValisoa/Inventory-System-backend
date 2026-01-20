package mg.tsiry.invetory_management_system.data.entities;

import jakarta.persistence.*;
import lombok.*;
import mg.tsiry.invetory_management_system.enums.TransactionStatus;
import mg.tsiry.invetory_management_system.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tsiry Valisoa
 */
@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private Integer totalProducts;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String description;
    private LocalDateTime updatedAt;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @ToString.Exclude
    private Supplier supplier;

}
