package com.uberclone.backend.service;

import com.uberclone.backend.Entity.User;
import com.uberclone.backend.dto.CreateUserRequest;
import com.uberclone.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(CreateUserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNo());
        userRepository.save(user);
        return "User created"  + request.getName();
    }
}
