package com.onlyflans.bakery.controller;

import com.onlyflans.bakery.model.User.DTO.UserCreateRequest;
import com.onlyflans.bakery.model.User.DTO.UserUpdateRequest;
import com.onlyflans.bakery.model.User.User;
import com.onlyflans.bakery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    // GET
    @GetMapping("/{rut}")
    public ResponseEntity<User> getUser(@PathVariable String rut) {
        User user = userService.getUser(rut);
        return ResponseEntity.ok(user);
    }

    // POST
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreateRequest createRequest) {
        User createdUser = userService.createUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // PUT
    @PutMapping("/{rut}")
    public ResponseEntity<User> updateUser(@PathVariable String rut,
                                           @RequestBody @Valid UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(rut, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // PATCH
    @PatchMapping("/{rut}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable String rut,
                                                  @RequestBody UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(rut, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> deleteUser(@PathVariable String rut) {
        userService.deleteUser(rut);
        return ResponseEntity.noContent().build();
    }
}
