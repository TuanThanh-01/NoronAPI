package com.mhsolution.noronapi.service.vote;

import com.mhsolution.noronapi.data.request.VoteRequest;
import com.mhsolution.noronapi.data.response.VoteResponse;

public interface VoteService {

    VoteResponse createVotePost(VoteRequest voteRequest);

    VoteResponse createVoteComment(VoteRequest voteRequest);

    void deleteVote(int voteId);

}
