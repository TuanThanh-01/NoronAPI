package com.mhsolution.noronapi.repository.post;

import com.mhsolution.noronapi.data.response.PostResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Post;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;

import java.util.List;

public interface PostRepository {

    List<Post> findAll(int pageNum, int limit);

    Post findById(int postId);

    void add(Post post, List<Integer> listTopic);

    void update(Post post, int postId);

    void delete(int postId);
}
