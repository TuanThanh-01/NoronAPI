package com.mhsolution.noronapi.controller;

import com.mhsolution.noronapi.data.request.CommentRequest;
import com.mhsolution.noronapi.data.response.CommentResponse;
import com.mhsolution.noronapi.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comment/get-all-by-post/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentInPost(@PathVariable("postId") int postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.fetchAllCommentByPostId(postId));
    }

    @GetMapping("/comment/get-all-answer-comment/{cmtId}")
    public ResponseEntity<List<CommentResponse>> getAllAnswerComment(@PathVariable("cmtId") int cmtId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.fetchAllAnswerComment(cmtId));
    }

    @PostMapping("/comment/create")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentRequest));
    }

    @PutMapping("/comment/update/{id}")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest commentRequest, @PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(id, commentRequest));
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") int id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete comment success!!!");
    }

}
