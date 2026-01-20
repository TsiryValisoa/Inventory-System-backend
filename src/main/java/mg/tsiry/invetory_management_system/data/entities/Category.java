package mg.tsiry.invetory_management_system.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Tsiry Valisoa
 */
@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category",  cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Product> products;
}
