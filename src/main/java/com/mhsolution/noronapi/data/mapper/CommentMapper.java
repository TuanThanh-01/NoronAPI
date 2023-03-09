package com.mhsolution.noronapi.data.mapper;


import com.mhsolution.noronapi.data.request.CommentRequest;
import com.mhsolution.noronapi.data.response.CommentResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Comments;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    private UserMapper userMapper;

    public abstract Comments toPojo(CommentRequest commentRequest);

    public abstract CommentResponse toCommentResponse(Comments comments);

    public abstract CommentResponse toResponse(Comments comments, @Context Users user);

    @AfterMapping
    protected void after(@MappingTarget Comments comment, CommentRequest commentRequest) {
        comment.setCreateAt(getCurrentDateTime());
        comment.setUpdateAt(null);
        comment.setDeleteAt(null);
        comment.setNumVote(0);
    }

    @AfterMapping
    protected void after(@MappingTarget CommentResponse commentResponse, Comments comments, @Context Users users) {
        commentResponse.setUser(userMapper.toResponse(users));
    }

}
