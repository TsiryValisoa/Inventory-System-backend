package mg.tsiry.invetory_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.User;
import mg.tsiry.invetory_management_system.data.repositories.UserRepository;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.enums.UserRole;
import mg.tsiry.invetory_management_system.exception.InvalidCredentialsException;
import mg.tsiry.invetory_management_system.exception.NameValueRequiredException;
import mg.tsiry.invetory_management_system.exception.NotFoundException;
import mg.tsiry.invetory_management_system.security.JwtUtils;
import mg.tsiry.invetory_management_system.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A class that manages user's information and authentication.
 *
 * @author Tsiry Valisoa
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @Override
    public GlobalResponse addUser(UserDto userDto) {

        checkDuplicateName(userDto.getId(), userDto.getName());
        checkDuplicateEmail(userDto.getId(), userDto.getEmail());
        checkDuplicatePhoneNumber(userDto.getId(), userDto.getPhoneNumber());

        UserRole defaultRole = UserRole.MANAGER;

        if (userDto.getRole() != null) {
            defaultRole = userDto.getRole();
        }

        User userToSave = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .role(defaultRole)
                .isActive(true)
                .build();

        userRepository.save(userToSave);

        return GlobalResponse.builder()
                .status(200)
                .message("User created successfully.")
                .user(userDto)
                .build();
    }

    @Override
    public GlobalResponse loginUser(UserDto userDto) {

        User userMail = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found!"));

        if (!userMail.isActive()) {
            throw new InvalidCredentialsException("This user is disabled");
        }

        if (!passwordEncoder.matches(userDto.getPassword(), userMail.getPassword())) {
            throw new InvalidCredentialsException("password does not match!");
        }
        String token = jwtUtils.generateToken(userMail.getEmail());

        return GlobalResponse.builder()
                .status(200)
                .message("User logged successfully.")
                .role(userMail.getRole())
                .token(token)
                .expirationTime("6 months.")
                .build();
    }

    @Override
    public GlobalResponse getAllUsers(int page, int size, String search) {

        Page<User> userPage = pageSearchList(page, size, search);

        List<User> filterUser = userPage.getContent().stream()
                .filter(user -> !"rotsiniaina.tsiry@gmail.com".equalsIgnoreCase(user.getEmail()))
                .filter(user -> user.isActive())
                .toList();
        List<UserDto> userDtoList = modelMapper.map(filterUser, new TypeToken<List<UserDto>>() {}.getType());

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .currentPage(page)
                .totalElement(userPage.getTotalElements())
                .totalPage(userPage.getTotalPages())
                .users(userDtoList)
                .build();
    }

    @Override
    public UserDto getCurrentLoggedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User userMail = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        userMail.setTransactions(null);

        return modelMapper.map(userMail, UserDto.class);
    }

    @Override
    public GlobalResponse updateUser(Long id, UserDto userDto) {

        checkDuplicateName(id, userDto.getName());
        checkDuplicateEmail(id, userDto.getEmail());
        checkDuplicatePhoneNumber(id, userDto.getPhoneNumber());

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setRole(userDto.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(existingUser);

        return GlobalResponse.builder()
                .status(200)
                .message("User successfully updated.")
                .build();
    }

    @Override
    @Transactional
    public GlobalResponse deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        userRepository.delete(user);

        return GlobalResponse.builder()
                .status(200)
                .message("User successfully deleted!")
                .build();
    }

    @Override
    public GlobalResponse getUserTransaction(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .user(userDto)
                .build();
    }

    @Override
    public GlobalResponse disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        user.setDisabledBy(name);
        user.setActive(false);

        userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Account disabled successfully.")
                .user(userDto)
                .build();
    }

    @Override
    public GlobalResponse listAllDisabledUser(int page, int size, String search) {

        Page<User> userPage = pageSearchList(page, size, search);

        List<User> filterUser = userPage.getContent().stream()
                .filter(user -> !user.isActive())
                .toList();
        List<UserDto> userDtoList = modelMapper.map(filterUser, new TypeToken<List<UserDto>>() {}.getType());

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .currentPage(page)
                .totalElement(userPage.getTotalElements())
                .totalPage(userPage.getTotalPages())
                .users(userDtoList)
                .build();
    }

    @Override
    public GlobalResponse enableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        user.setEnabledBy(name);
        user.setActive(true);

        userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Account enabled successfully.")
                .user(userDto)
                .build();
    }

    private void checkDuplicateName(Long id, String name) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent() && !user.get().getId().equals(id)) {
            throw new NameValueRequiredException("This name already exist!");
        }
    }

    private void checkDuplicateEmail(Long id, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && !user.get().getId().equals(id)) {
            throw new NameValueRequiredException("This email already exist!");
        }
    }

    private void checkDuplicatePhoneNumber(Long id, String phoneNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent() && !user.get().getId().equals(id)) {
            throw new NameValueRequiredException("This phone number already exist!");
        }
    }

    public Page<User> pageSearchList(int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (search != null && !search.isEmpty()) {
            return userRepository.findByNameOrRole(search, pageable);
        }

        return userRepository.findAll(pageable);
    }
}
