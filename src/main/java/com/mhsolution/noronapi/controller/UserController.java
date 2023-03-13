package com.mhsolution.noronapi.controller;


import com.mhsolution.noronapi.data.mapper.UserMapper;
import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.mhsolution.noronapi.entity.ListUserResponse;
import com.mhsolution.noronapi.service.user.UserService;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user/all")
    public ResponseEntity<Single<ListUserResponse>> getAllUser(@RequestParam int limit, @RequestParam int pageNum) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUser(pageNum, limit));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Single<UserResponse>> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.findUserById(id)
                        .subscribeOn(Schedulers.io())
                        .map(users -> userMapper.toResponse(users)));
    }

    @PostMapping("/user/create")
    public ResponseEntity<Single<UserResponse>> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userService.createUser(userRequest)
                        .subscribeOn(Schedulers.io())
                        .map(users -> userMapper.toResponse(users)));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<Single<UserResponse>> updateUser(@PathVariable("id") int userId, @RequestBody UserRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.updateUserInfo(userId, user)
                        .subscribeOn(Schedulers.io())
                        .map(users -> userMapper.toResponse(users)));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete user success");
    }

}
