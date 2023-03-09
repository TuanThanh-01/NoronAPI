package com.mhsolution.noronapi.data.mapper;

import com.mhsolution.noronapi.data.request.VoteRequest;
import com.mhsolution.noronapi.data.response.VoteResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Vote;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VoteMapper {

    @Autowired
    protected UserMapper userMapper;

    public abstract Vote toPojo(VoteRequest voteRequest);

    public abstract VoteResponse toVoteResponse(Vote vote);

    public abstract VoteResponse toVoteResponse(Vote vote,@Context String voteType, @Context int contentId, @Context Users user);

    @AfterMapping
    protected void after(@MappingTarget Vote vote, VoteRequest voteRequest) {
        vote.setCreateAt(getCurrentDateTime());
        vote.setUpdateAt(null);
        vote.setDeleteAt(null);
    }

    @AfterMapping
    protected void after(@MappingTarget VoteResponse voteResponse,
                         Vote vote,
                         @Context String voteType,
                         @Context int contentId,
                         @Context Users user) {
        voteResponse.setVoteType(voteType);
        voteResponse.setContentId(contentId);
        voteResponse.setUser(userMapper.toResponse(user));
    }
}
