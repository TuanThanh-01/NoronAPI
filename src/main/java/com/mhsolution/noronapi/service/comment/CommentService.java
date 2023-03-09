package com.mhsolution.noronapi.service.comment;



import com.mhsolution.noronapi.data.request.CommentRequest;
import com.mhsolution.noronapi.data.response.CommentResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Comments;

import java.util.List;

public interface CommentService {

    List<CommentResponse> fetchAllCommentByPostId(int postId);

    List<CommentResponse> fetchAllAnswerComment(int cmtId);

    CommentResponse createComment(CommentRequest commentRequest);

    CommentResponse updateComment(int cmtId, CommentRequest commentRequest);

    void deleteComment(int cmtId);

}
