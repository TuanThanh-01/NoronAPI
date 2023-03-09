package com.mhsolution.noronapi.service.user;

import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.mhsolution.noronapi.entity.ListUserResponse;


import java.util.List;

public interface UserService {

    boolean checkUserExist(String email);

    ListUserResponse findAllUser(int pageNum, int limit);

    UserResponse findUserById(int id);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUserInfo(int userId, UserRequest userRequest);

    void deleteUser(int userId);
}
