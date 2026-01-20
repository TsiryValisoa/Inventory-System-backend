package mg.tsiry.invetory_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.request.RegisterRequest;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tsiry Valisoa
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> getAllUser(@RequestParam int page,
                                                     @RequestParam int size,
                                                     @RequestParam(required = false) String search) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, search));
    }

    @GetMapping("/current")
    public ResponseEntity<?> listCurrentLoggedUser() {
        return ResponseEntity.ok(userService.getCurrentLoggedUser());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GlobalResponse> updateUser(@PathVariable Long id,
                                                     @RequestBody @Valid RegisterRequest registerRequest) {
        UserDto userDto = modelMapper.map(registerRequest, UserDto.class);
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<GlobalResponse> listUserAndTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserTransaction(id));
    }

    @PutMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> disableUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.disableUser(id));
    }

    @GetMapping("/all-disabled")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> listAllDisableUser(@RequestParam int page,
                                                             @RequestParam int size,
                                                             @RequestParam(required = false) String search) {
        return ResponseEntity.ok(userService.listAllDisabledUser(page, size, search));
    }

    @PutMapping("/enable/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> enableUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.enableUser(id));
    }
}
