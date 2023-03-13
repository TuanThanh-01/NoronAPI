package com.mhsolution.noronapi.service.post;

import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.response.PostResponse;
import com.mhsolution.noronapi.entity.ListPostResponse;
import io.reactivex.rxjava3.core.Single;

import java.util.HashMap;


public interface PostService {

    Single<ListPostResponse> findAll(int pageNum, int limit);

    Single<PostResponse> findPostById(int postId);

    Single<PostResponse> createPost(PostRequest postRequest);

    Single<PostResponse> updatePost(int postId, PostRequest postRequest);

    void deletePost(int postId);

}
