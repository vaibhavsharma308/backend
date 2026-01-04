package com.uberclone.backend.controller;


import com.uberclone.backend.entity.User;
import com.uberclone.backend.dto.*;
import com.uberclone.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public String createUser(@RequestBody @Valid CreateUserRequest request) {
        return userService.createUser(request);
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @PatchMapping("/{id}")
    public User patchUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
    @GetMapping("/me")
    public UserProfileResponse getMyProfile(
            @RequestParam Long userId) {
        return userService.getUserProfile(userId);
    }
    @PutMapping("/me")
    public UserProfileResponse updateMyProfile(
            @RequestParam Long userId,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        return userService.updateUserProfile(userId, request);
    }
}
