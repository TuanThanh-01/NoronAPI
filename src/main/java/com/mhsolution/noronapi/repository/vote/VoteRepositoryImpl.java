package com.mhsolution.noronapi.repository.vote;

import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.Tables;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Vote;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class VoteRepositoryImpl implements VoteRepository {

    private final DSLContext dslContext;

    private final TimeUtils currentDateTime;

    public VoteRepositoryImpl(DSLContext dslContext, TimeUtils currentDateTime) {
        this.dslContext = dslContext;
        this.currentDateTime = currentDateTime;
    }

    @Override
    public void addVotePost(Vote vote, int postId) {
        dslContext.transaction(out -> {
            DSLContext context = DSL.using(out);
            Vote voteAdded = context.insertInto(Tables.VOTE,
                            Tables.VOTE.USER_ID,
                            Tables.VOTE.CREATE_AT,
                            Tables.VOTE.UPDATE_AT,
                            Tables.VOTE.DELETE_AT)
                    .values(vote.getUserId(),
                            vote.getCreateAt(),
                            vote.getUpdateAt(),
                            vote.getDeleteAt())
                    .returning()
                    .fetchOneInto(Vote.class);

            context.update(Tables.POST)
                    .set(Tables.POST.NUM_VOTE, Tables.POST.NUM_VOTE.plus(1))
                    .where(Tables.POST.ID.eq(postId))
                    .execute();

            context.insertInto(Tables.VOTE_POST,
                            Tables.VOTE_POST.POST_ID,
                            Tables.VOTE_POST.VOTE_ID)
                    .values(postId,
                            voteAdded.getId())
                    .execute();
        });
    }

    @Override
    public void addVoteComment(Vote vote, int cmtId) {
        dslContext.transaction(out -> {
            DSLContext context = DSL.using(out);
            Vote voteAdded = context.insertInto(Tables.VOTE,
                            Tables.VOTE.USER_ID,
                            Tables.VOTE.CREATE_AT,
                            Tables.VOTE.UPDATE_AT,
                            Tables.VOTE.DELETE_AT)
                    .values(vote.getUserId(),
                            vote.getCreateAt(),
                            vote.getUpdateAt(),
                            vote.getDeleteAt())
                    .returning()
                    .fetchOneInto(Vote.class);

            context.update(Tables.COMMENTS)
                    .set(Tables.COMMENTS.NUM_VOTE, Tables.COMMENTS.NUM_VOTE.plus(1))
                    .where(Tables.COMMENTS.ID.eq(cmtId))
                    .execute();

            context.insertInto(Tables.VOTE_COMMENT,
                            Tables.VOTE_COMMENT.VOTE_ID,
                            Tables.VOTE_COMMENT.COMMENT_ID)
                    .values(voteAdded.getId(),
                            cmtId)
                    .execute();
        });
    }

    @Override
    public void deleteVote(int voteId) {
        dslContext.update(Tables.VOTE)
                .set(Tables.VOTE.DELETE_AT, currentDateTime.getCurrentDateTime())
                .where(Tables.VOTE.ID.eq(voteId))
                .execute();
    }
}
