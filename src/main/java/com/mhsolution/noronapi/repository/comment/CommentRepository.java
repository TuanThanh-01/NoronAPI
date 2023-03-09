package com.mhsolution.noronapi.repository.comment;

import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Comments;

import java.util.List;

public interface CommentRepository {

    List<Comments> findAllByPostId(int postId);

    List<Comments> findAllAnswerComment(int commentId);

    Comments findById(int commentId);

    void add(Comments comment);

    void update(Comments comment, int commentId);

    void delete(int commentId);
}
