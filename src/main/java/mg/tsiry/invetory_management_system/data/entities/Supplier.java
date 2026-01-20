package mg.tsiry.invetory_management_system.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tsiry Valisoa
 */
@Entity
@Table(name = "suppliers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
}
