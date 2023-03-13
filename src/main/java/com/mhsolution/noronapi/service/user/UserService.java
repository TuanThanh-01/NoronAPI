package com.mhsolution.noronapi.service.user;

import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.mhsolution.noronapi.entity.ListUserResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import io.reactivex.rxjava3.core.Single;


import java.util.List;

public interface UserService {

    boolean checkUserExist(String email);

    Single<ListUserResponse> findAllUser(int pageNum, int limit);

    Single<Users> findUserById(int id);

    Single<Users> createUser(UserRequest userRequest);

    Single<Users> updateUserInfo(int userId, UserRequest userRequest);

    void deleteUser(int userId);
}
