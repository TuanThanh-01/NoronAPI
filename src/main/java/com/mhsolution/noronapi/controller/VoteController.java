package com.mhsolution.noronapi.controller;

import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.request.VoteRequest;
import com.mhsolution.noronapi.data.response.VoteResponse;
import com.mhsolution.noronapi.service.vote.VoteService;
import com.tej.JooQDemo.jooq.sample.model.tables.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/vote/create-vote-post")
    public ResponseEntity<VoteResponse> createVotePost(@RequestBody VoteRequest voteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteService.createVotePost(voteRequest));
    }

    @PostMapping("/vote/create-vote-comment")
    public ResponseEntity<VoteResponse> createVoteComment(@RequestBody VoteRequest voteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteService.createVoteComment(voteRequest));
    }

    @DeleteMapping("/vote/delete/{id}")
    public ResponseEntity<String> deleteVote(@PathVariable("id") int id) {
        voteService.deleteVote(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete vote success");
    }
}
