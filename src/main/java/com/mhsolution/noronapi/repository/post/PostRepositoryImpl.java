package com.mhsolution.noronapi.repository.post;

import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.Tables;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Post;
import com.tej.JooQDemo.jooq.sample.model.tables.records.PostTopicRecord;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final DSLContext dslContext;

    private final TimeUtils currentDateTime;

    public PostRepositoryImpl(DSLContext dslContext, TimeUtils currentDateTime) {
        this.dslContext = dslContext;
        this.currentDateTime = currentDateTime;
    }

    @Override
    public List<Post> findAll(int pageNum, int limit) {
        return dslContext.selectFrom(Tables.POST)
                .where(Tables.POST.DELETE_AT.isNull())
                .limit(limit)
                .offset((pageNum - 1) * limit)
                .fetchInto(Post.class);
    }

    @Override
    public Post findById(int postId) {
        dslContext.update(Tables.POST)
                .set(Tables.POST.NUM_VIEW, Tables.POST.NUM_VIEW.plus(1))
                .where(Tables.POST.ID.eq(postId))
                .execute();

        return dslContext.selectFrom(Tables.POST)
                .where(Tables.POST.ID.eq(postId))
                .fetchOneInto(Post.class);
    }

    @Override
    public void add(Post post, List<Integer> topicList) {

        dslContext.transaction(out -> {
            DSLContext context = DSL.using(out);
            Post postAdded = context.insertInto(Tables.POST,
                            Tables.POST.TITLE,
                            Tables.POST.POST_TYPE,
                            Tables.POST.CONTENT,
                            Tables.POST.NUM_VIEW,
                            Tables.POST.CREATE_AT,
                            Tables.POST.UPDATE_AT,
                            Tables.POST.DELETE_AT,
                            Tables.POST.USER_ID,
                            Tables.POST.NUM_VOTE,
                            Tables.POST.NUM_COMMENT)
                    .values(post.getTitle(),
                            post.getPostType(),
                            post.getContent(),
                            post.getNumView(),
                            post.getCreateAt(),
                            post.getUpdateAt(),
                            post.getDeleteAt(),
                            post.getUserId(),
                            post.getNumVote(),
                            post.getNumComment())
                    .returning()
                    .fetchOneInto(Post.class);

            List<InsertSetMoreStep<PostTopicRecord>> insertValuesStep2s = topicList.stream()
                    .map(topic -> context
                            .insertInto(Tables.POST_TOPIC)
                            .set(Tables.POST_TOPIC.POST_ID, postAdded.getId())
                            .set(Tables.POST_TOPIC.TOPIC_ID, topic))
                    .collect(Collectors.toList());
            context.batch(insertValuesStep2s).execute();
        });

    }

    @Override
    public void update(Post post, int postId) {
        dslContext.update(Tables.POST)
                .set(Tables.POST.TITLE, post.getTitle())
                .set(Tables.POST.CONTENT, post.getContent())
                .set(Tables.POST.UPDATE_AT, post.getUpdateAt())
                .where(Tables.POST.ID.eq(postId))
                .execute();
    }

    @Override
    public void delete(int postId) {
        dslContext.update(Tables.POST)
                .set(Tables.POST.DELETE_AT, currentDateTime.getCurrentDateTime())
                .where(Tables.POST.ID.eq(postId))
                .execute();
    }
}
