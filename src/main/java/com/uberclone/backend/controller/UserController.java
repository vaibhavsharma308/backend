package com.uberclone.backend.controller;


import com.uberclone.backend.dto.CreateUserRequest;
import com.uberclone.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public String createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id) {
        return "Getting Driver with iD "+ id ;
    }
}
