package com.mhsolution.noronapi.service.post;

import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.response.PostResponse;
import com.mhsolution.noronapi.entity.ListPostResponse;

import java.util.HashMap;


public interface PostService {

    ListPostResponse findAll(int pageNum, int limit);

    PostResponse findPostById(int postId);

    PostResponse createPost(PostRequest postRequest);

    PostResponse updatePost(int postId, PostRequest postRequest);

    void deletePost(int postId);

}
