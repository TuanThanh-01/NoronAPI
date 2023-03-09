package com.mhsolution.noronapi.data.mapper;


import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.response.PostResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Post;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected TopicMapper topicMapper;

    public abstract Post toPojo(PostRequest postRequest);

    public abstract PostResponse toPostResponse(Post post);

    @Named("toRs")
    public abstract PostResponse toPostResponse(Post post,
                                                @Context Map<Integer, List<Topic>> postTopicMap,
                                                @Context Map<Integer, Users> userMap);

    public abstract PostResponse toPostResponse(Post post, @Context Users user, @Context List<Topic> topic);

    @IterableMapping(qualifiedByName = "toRs")
    public abstract List<PostResponse> toPostResponse(List<Post> posts,
                                                      @Context Map<Integer, List<Topic>> postTopicMap,
                                                      @Context Map<Integer, Users> userMap);

    @AfterMapping
    protected void after(@MappingTarget Post post, PostRequest request) {
        post.setCreateAt(getCurrentDateTime());
        post.setUpdateAt(null);
        post.setDeleteAt(null);
        post.setNumView(0);
        post.setNumComment(0);
        post.setNumVote(0);
    }

    @AfterMapping
    protected void after(@MappingTarget PostResponse postResponse, Post post,
                         @Context Map<Integer, List<Topic>> postTopicMap,
                         @Context Map<Integer, Users> userMap) {

        postResponse.setUser(userMapper.toResponse(userMap.get(post.getUserId())));
        postResponse.setTopics(topicMapper.toTopicResponses(postTopicMap.get(post.getId())));
    }

    @AfterMapping
    protected void after(@MappingTarget PostResponse postResponse, Post post,
                         @Context Users users,
                         @Context List<Topic> topics) {
        postResponse.setTopics(topicMapper.toTopicResponses(topics));
        postResponse.setUser(userMapper.toResponse(users));
    }
}
