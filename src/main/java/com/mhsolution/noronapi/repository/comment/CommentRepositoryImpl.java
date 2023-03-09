package com.mhsolution.noronapi.repository.comment;

import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.Tables;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Comments;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository{

    private final DSLContext dslContext;

    private final TimeUtils currentDateTime;

    public CommentRepositoryImpl(DSLContext dslContext,
                                 TimeUtils currentDateTime) {
        this.dslContext = dslContext;
        this.currentDateTime = currentDateTime;
    }

    @Override
    public List<Comments> findAllByPostId(int postId) {
        return dslContext.selectFrom(Tables.COMMENTS)
                .where(Tables.COMMENTS.POST_ID.eq(postId))
                .and(Tables.COMMENTS.DELETE_AT.isNull())
                .fetchInto(Comments.class);
    }

    @Override
    public List<Comments> findAllAnswerComment(int commentId) {
        return dslContext.selectFrom(Tables.COMMENTS)
                .where(Tables.COMMENTS.PARENT_ID.eq(commentId))
                .and(Tables.COMMENTS.DELETE_AT.isNull())
                .fetchInto(Comments.class);
    }


    @Override
    public Comments findById(int commentId) {
        return dslContext.selectFrom(Tables.COMMENTS)
                .where(Tables.COMMENTS.ID.eq(commentId))
                .and(Tables.COMMENTS.DELETE_AT.isNull())
                .fetchOneInto(Comments.class);
    }

    @Override
    public void add(Comments comment) {

        dslContext.transaction(outer -> {
            DSLContext context = DSL.using(outer);
            context.insertInto(Tables.COMMENTS,
                            Tables.COMMENTS.CONTENT,
                            Tables.COMMENTS.COMMENT_TYPE,
                            Tables.COMMENTS.PARENT_ID,
                            Tables.COMMENTS.CREATE_AT,
                            Tables.COMMENTS.UPDATE_AT,
                            Tables.COMMENTS.DELETE_AT,
                            Tables.COMMENTS.USER_ID,
                            Tables.COMMENTS.POST_ID,
                            Tables.COMMENTS.NUM_VOTE)
                    .values(comment.getContent(),
                            comment.getCommentType(),
                            comment.getParentId(),
                            comment.getCreateAt(),
                            comment.getUpdateAt(),
                            comment.getDeleteAt(),
                            comment.getUserId(),
                            comment.getPostId(),
                            comment.getNumVote())
                    .execute();

            context.update(Tables.POST)
                    .set(Tables.POST.NUM_COMMENT,  Tables.POST.NUM_COMMENT.plus(1))
                    .where(Tables.POST.ID.eq(comment.getPostId()))
                    .execute();
        });



    }

    @Override
    public void update(Comments comment, int commentId) {
        dslContext.update(Tables.COMMENTS)
                .set(Tables.COMMENTS.CONTENT, comment.getContent())
                .set(Tables.COMMENTS.UPDATE_AT, comment.getUpdateAt())
                .where(Tables.COMMENTS.ID.eq(commentId))
                .execute();
    }

    @Override
    public void delete(int commentId) {
        dslContext.update(Tables.COMMENTS)
                .set(Tables.COMMENTS.DELETE_AT, currentDateTime.getCurrentDateTime())
                .where(Tables.COMMENTS.ID.eq(commentId))
                .execute();
    }
}
