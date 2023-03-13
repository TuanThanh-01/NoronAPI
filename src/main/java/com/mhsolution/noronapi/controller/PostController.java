package com.mhsolution.noronapi.controller;

import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.response.PostResponse;
import com.mhsolution.noronapi.entity.ListPostResponse;
import com.mhsolution.noronapi.service.post.PostService;

import io.reactivex.rxjava3.core.Single;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/all")
    public ResponseEntity<Single<ListPostResponse>> findAllPost(@RequestParam int limit, @RequestParam int pageNum) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAll(pageNum, limit));
//        postService.findAll(pageNum, limit);
//        return null;
    }

    @GetMapping("/post/find-single-post/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable("id") int postId) {
//        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostById(postId));
        postService.findPostById(postId);
        return null;
    }

    @PostMapping("/post/create")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest));
        postService.createPost(postRequest);
        return null;
    }

    @PutMapping("/post/update/{id}")
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostRequest postRequest, @PathVariable("id") int postId) {
//        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postId, postRequest));
        postService.updatePost(postId, postRequest);
        return null;
    }

    @DeleteMapping("/post/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") int postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete post success!!!");
    }
}
