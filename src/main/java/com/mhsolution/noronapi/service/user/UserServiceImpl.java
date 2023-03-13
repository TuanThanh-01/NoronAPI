package com.mhsolution.noronapi.service.user;

import com.mhsolution.noronapi.data.mapper.UserMapper;
import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.mhsolution.noronapi.entity.ListUserResponse;
import com.mhsolution.noronapi.repository.users.UserRepository;
import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final DSLContext context;

    private final TimeUtils currentDateTime;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(DSLContext context,
                           TimeUtils currentDateTime,
                           UserRepository userRepository,
                           UserMapper userMapper) {
        this.context = context;
        this.currentDateTime = currentDateTime;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean checkUserExist(String email) {
        return userRepository.usersExistsByEmail(email);
    }

    @Override
    public Single<ListUserResponse> findAllUser(int pageNum, int limit) {
        return Single.create(singleSubscriber -> {
            ListUserResponse listUserResponse = new ListUserResponse();
            listUserResponse.setLimit(limit)
                    .setPageNum(pageNum)
                    .setUsers(userMapper.toUserResponses(userRepository.findAll(pageNum, limit)));
            singleSubscriber.onSuccess(listUserResponse);
        });
    }

    @Override
    public Single<Users> findUserById(int id) {
         return Single.create(singleSubscriber -> {
             Users users = userRepository.findByID(id);
             if (users == null) {
                 singleSubscriber.onError(new Exception("user not found"));
             }
             else {
                 singleSubscriber.onSuccess(users);
             }
        });
    }

    @Override
    public Single<Users> createUser(UserRequest userRequest) {
        return Single.create(singleSubscriber -> {
            Users user = userMapper.toPojo(userRequest);
            if(checkUserExist(user.getEmail())) {
                singleSubscriber.onError(new Exception("User Email Exists!!!"));
            }
            else {
                userRepository.add(user);
                singleSubscriber.onSuccess(user);
            }
        });
    }

    @Override
    public Single<Users> updateUserInfo(int userId, UserRequest userRequest) {
        return Single.create(singleSubscriber -> {
            Users user = userRepository.findByID(userId);
            if(user == null) {
                singleSubscriber.onError(new Exception("User not found!!!"));
            }
            else {
                if (Objects.nonNull(userRequest.getUsername()) && !"".equalsIgnoreCase(userRequest.getUsername())) {
                    user.setUsername(userRequest.getUsername());
                }
                if (Objects.nonNull(userRequest.getPhoneNumber()) && !"".equalsIgnoreCase(userRequest.getPhoneNumber())) {
                    user.setPhoneNumber(userRequest.getPhoneNumber());
                }
                if (Objects.nonNull(userRequest.getPass()) && !"".equalsIgnoreCase(userRequest.getPass())) {
                    user.setPass(userRequest.getPass());
                }
                if (Objects.nonNull(userRequest.getEmail()) && !"".equalsIgnoreCase(userRequest.getEmail()) && !userRepository.usersExistsByEmail(userRequest.getEmail())) {
                    user.setEmail(userRequest.getEmail());
                }
                user.setUpdateAt(currentDateTime.getCurrentDateTime());
                userRepository.update(user, userId);
                singleSubscriber.onSuccess(user);
            }
        });
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.delete(userId);
    }
}
