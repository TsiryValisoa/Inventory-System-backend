package mg.tsiry.invetory_management_system.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.request.SupplierRequest;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.SupplierDto;
import mg.tsiry.invetory_management_system.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tsiry Valisoa
 */
@RestController
@RequestMapping("/api/suppliers")
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> addSupplier(@RequestBody @Valid SupplierRequest supplierRequest) {
        SupplierDto supplierDto = modelMapper.map(supplierRequest, SupplierDto.class);
        return ResponseEntity.ok(supplierService.addSupplier(supplierDto));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> listAllSupplier(@RequestParam int page,
                                                          @RequestParam int size,
                                                          @RequestParam(required = false) String search) {
        return ResponseEntity.ok(supplierService.getAllSupplier(page, size, search));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> updateSupplier(@PathVariable Long id,
                                                         @RequestBody @Valid SupplierRequest supplierRequest) {
        SupplierDto supplierDto = modelMapper.map(supplierRequest, SupplierDto.class);
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> listById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> deleteSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.deleteSupplier(id));
    }

}
