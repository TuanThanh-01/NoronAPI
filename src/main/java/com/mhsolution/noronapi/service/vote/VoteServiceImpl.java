package com.mhsolution.noronapi.service.vote;

import com.mhsolution.noronapi.data.mapper.UserMapper;
import com.mhsolution.noronapi.data.mapper.VoteMapper;
import com.mhsolution.noronapi.data.request.VoteRequest;
import com.mhsolution.noronapi.data.response.VoteResponse;
import com.mhsolution.noronapi.repository.users.UserRepository;
import com.mhsolution.noronapi.repository.vote.VoteRepository;
import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Vote;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final TimeUtils timeUtils;

    public VoteServiceImpl(VoteRepository voteRepository, VoteMapper voteMapper, UserMapper userMapper, UserRepository userRepository, TimeUtils timeUtils) {
        this.voteRepository = voteRepository;
        this.voteMapper = voteMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.timeUtils = timeUtils;
    }

    @Override
    public VoteResponse createVotePost(VoteRequest voteRequest) {
        Vote vote = voteMapper.toPojo(voteRequest);
        voteRepository.addVotePost(vote, voteRequest.getContentId());
        Users user = userRepository.findByID(voteRequest.getUserId());
        return voteMapper.toVoteResponse(vote, "Post", voteRequest.getContentId(), user);
    }

    @Override
    public VoteResponse createVoteComment(VoteRequest voteRequest) {
        Vote vote = voteMapper.toPojo(voteRequest);
        voteRepository.addVoteComment(vote, voteRequest.getContentId());
        Users user = userRepository.findByID(voteRequest.getUserId());
        return voteMapper.toVoteResponse(vote, "Comment", voteRequest.getContentId(), user);
    }



    @Override
    public void deleteVote(int voteId) {
        voteRepository.deleteVote(voteId);
    }


}
