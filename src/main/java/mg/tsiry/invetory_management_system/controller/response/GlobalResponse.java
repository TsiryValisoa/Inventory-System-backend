package mg.tsiry.invetory_management_system.controller.response;

import lombok.Builder;
import lombok.Data;
import mg.tsiry.invetory_management_system.dto.*;
import mg.tsiry.invetory_management_system.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GlobalResponse {

    //Generic
    private int status;
    private String message;

    //Login
    private String token;
    private UserRole role;
    private String expirationTime;

    //Pagination
    private Integer currentPage;
    private Long totalElement;
    private Integer totalPage;

    //Data output optional
    private UserDto user;
    private List<UserDto> users;
    private SupplierDto supplier;
    private List<SupplierDto> suppliers;
    private CategoryDto category;
    private List<CategoryDto> categories;
    private ProductDto product;
    private List<ProductDto> products;
    private TransactionDto transaction;
    private List<TransactionDto> transactions;
    private RefreshTokenDto refreshToken;

    private final LocalDateTime timestamp = LocalDateTime.now();

}
