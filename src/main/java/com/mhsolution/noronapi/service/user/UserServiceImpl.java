package com.mhsolution.noronapi.service.user;

import com.mhsolution.noronapi.data.mapper.UserMapper;
import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.mhsolution.noronapi.entity.ListUserResponse;
import com.mhsolution.noronapi.repository.users.UserRepository;
import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
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
        return false;
    }

    @Override
    public ListUserResponse findAllUser(int pageNum, int limit) {
        ListUserResponse listUserResponse = new ListUserResponse();
        return listUserResponse.setLimit(limit)
                .setPageNum(pageNum)
                .setUsers(userMapper.toUserResponses(userRepository.findAll(pageNum, limit)));
    }

    @Override
    public UserResponse findUserById(int id) {
        return userMapper.toResponse(userRepository.findByID(id));
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        Users user = userMapper.toPojo(userRequest);
        userRepository.add(user);
        UserResponse userResponse = userMapper.toResponse(user);
        return userResponse;
    }

    @Override
    public UserResponse updateUserInfo(int userId, UserRequest userRequest) {
        Users user = userRepository.findByID(userId);
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
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.delete(userId);
    }
}
