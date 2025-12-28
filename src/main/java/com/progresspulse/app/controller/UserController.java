package com.progresspulse.app.controller;

import com.progresspulse.app.dto.UserDTO;
import com.progresspulse.app.model.User;
import com.progresspulse.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setDateOfBirth(userDTO.dateOfBirth());
        user.setHeightCm(userDTO.heightCm());
        user.setGender(userDTO.gender());

        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO dto = new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getDateOfBirth(),
                user.getHeightCm(),
                user.getGender()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> dtos = userService.getAllUsers()
                .stream()
                .map(u -> new UserDTO(
                        u.getName(),
                        u.getEmail(),
                        u.getDateOfBirth(),
                        u.getHeightCm(),
                        u.getGender()
                ))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
