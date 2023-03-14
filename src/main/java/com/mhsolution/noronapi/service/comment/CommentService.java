package com.mhsolution.noronapi.service.comment;



import com.mhsolution.noronapi.data.request.CommentRequest;
import com.mhsolution.noronapi.data.response.CommentResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Comments;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface CommentService {

    Single<List<CommentResponse>> fetchAllCommentByPostId(int postId);

    Single<List<CommentResponse>> fetchAllAnswerComment(int cmtId);

    Single<CommentResponse> createComment(CommentRequest commentRequest);

    Single<CommentResponse> updateComment(int cmtId, CommentRequest commentRequest);

    void deleteComment(int cmtId);

}
