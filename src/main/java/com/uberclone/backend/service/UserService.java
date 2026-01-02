package com.uberclone.backend.service;

import com.uberclone.backend.Entity.User;
import com.uberclone.backend.dto.*;
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
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNo());
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return "User created"  + request.getFirstname() + " " + request.getLastname();
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
            user.setFirstName(request.getName());
            user.setLastName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPhoneNo() != null) {
            user.setPhoneNumber(request.getPhoneNo());
        }
        return userRepository.save(user);
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setFirstname(user.getFirstName());
        response.setLastname(user.getLastName());
        response.setEmail(user.getEmail());

        return response;
    }

    public UserProfileResponse getUserProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setFirstname(user.getFirstName());
        response.setLastname(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNo(user.getPhoneNumber());
        return response;
    }

    public UserProfileResponse updateUserProfile(
            Long userId,
            UpdateUserProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setPhoneNumber(request.getPhoneNo());
        User savedUser = userRepository.save(user);

        UserProfileResponse response = new UserProfileResponse();
        response.setId(savedUser.getId());
        response.setFirstname(savedUser.getFirstName());
        response.setLastname(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setPhoneNo(savedUser.getPhoneNumber());
        return response;
    }

}
