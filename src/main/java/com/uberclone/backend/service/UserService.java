package com.uberclone.backend.service;

import com.uberclone.backend.Entity.User;
import com.uberclone.backend.dto.CreateUserRequest;
import com.uberclone.backend.dto.UpdateUserRequest;
import com.uberclone.backend.exception.ResourceNotFoundException;
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
    public User getUserById(Long Id){
        return userRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + Id
                ));
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPhoneNo() != null) {
            user.setPhoneNumber(request.getPhoneNo());
        }
        return userRepository.save(user);
    }

}
