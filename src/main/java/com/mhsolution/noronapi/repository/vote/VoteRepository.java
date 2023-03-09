package com.mhsolution.noronapi.repository.vote;

import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Vote;

public interface VoteRepository {

    void addVotePost(Vote vote, int postId);

    void addVoteComment(Vote vote, int cmtId);

    void deleteVote(int voteId);


}
