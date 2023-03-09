package com.mhsolution.noronapi.controller;


import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.mhsolution.noronapi.entity.ListUserResponse;
import com.mhsolution.noronapi.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/all")
    public ResponseEntity<ListUserResponse> getAllUser(@RequestParam int limit, @RequestParam int pageNum) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUser(pageNum, limit));
    }

    @PostMapping("/user/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") int userId, @RequestBody UserRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserInfo(userId, user));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete user success");
    }

}
