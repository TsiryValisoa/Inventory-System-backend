package mg.tsiry.invetory_management_system.controller;

import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import mg.tsiry.invetory_management_system.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Tsiry Valisoa
 */
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> addProduct(@RequestParam("imageFile") MultipartFile imageFile,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("sku") String sku,
                                                     @RequestParam("price") BigDecimal price,
                                                     @RequestParam("stockQuantity") Integer stockQuantity,
                                                     @RequestParam("categoryId") Long categoryId,
                                                     @RequestParam(value = "description", required = false) String description
    ) {

        ProductDto productDto = new ProductDto();

        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setStockQuantity(stockQuantity);
        productDto.setCategoryId(categoryId);
        productDto.setDescription(description);

        return ResponseEntity.ok(productService.addProduct(productDto, imageFile));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> listAllProduct(@RequestParam int page,
                                                         @RequestParam int size,
                                                         @RequestParam(required = false) String search,
                                                         @RequestParam(required = false) List<Long> categoryId) {
        return ResponseEntity.ok(productService.getAllProducts(page,size , search, categoryId));
    }

    @GetMapping("/image/{productId}")
    public ResponseEntity<Resource> getImageProduct(@PathVariable Long productId) throws MalformedURLException {

        Resource resource = productService.getProductImageById(productId);

        String filename = resource.getFilename();
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        String contentType = switch (extension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            case "png" -> MediaType.IMAGE_PNG_VALUE;
            case "gif" -> MediaType.IMAGE_GIF_VALUE;
            default -> "application/octet-stream";
        };

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> listProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getAllProductById(id));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "expiryDate", required = false) LocalDate expiryDate
    ) {

        ProductDto productDto = new ProductDto();

        productDto.setProductId(id);
        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setStockQuantity(stockQuantity);
        productDto.setCategoryId(categoryId);
        productDto.setDescription(description);
        productDto.setExpiryDate(expiryDate);

        return ResponseEntity.ok(productService.updateProduct(productDto, imageFile));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

}
