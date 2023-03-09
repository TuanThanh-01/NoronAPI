package com.mhsolution.noronapi.data.mapper;

import com.mhsolution.noronapi.data.request.UserRequest;
import com.mhsolution.noronapi.data.response.UserResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import org.mapstruct.*;

import java.util.List;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract Users toPojo(UserRequest userRequest);

    @Named("toResponse")
    public abstract UserResponse toResponse(Users user);

    @IterableMapping(qualifiedByName = "toResponse")
    public abstract List<UserResponse> toUserResponses(List<Users> users);

    @AfterMapping
    protected void after(@MappingTarget Users user, UserRequest userRequest) {
        user.setAvatar("default.png");
        user.setCreateAt(getCurrentDateTime());
        user.setUpdateAt(null);
        user.setDeleteAt(null);
    }
}
