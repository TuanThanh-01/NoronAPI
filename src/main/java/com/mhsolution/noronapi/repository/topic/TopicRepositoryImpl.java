package com.mhsolution.noronapi.repository.topic;

import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.Tables;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class TopicRepositoryImpl implements TopicRepository {

    private final DSLContext dslContext;

    private final TimeUtils currentDateTime;

    public TopicRepositoryImpl(DSLContext dslContext,
                               TimeUtils currentDateTime) {
        this.dslContext = dslContext;
        this.currentDateTime = currentDateTime;
    }


    @Override
    public List<Topic> findAll(int pageNum, int limit) {
        return dslContext.selectFrom(Tables.TOPIC)
                .where(Tables.TOPIC.DELETE_AT.isNull())
                .limit(limit)
                .offset((pageNum - 1) * limit)
                .fetchInto(Topic.class);
    }

    @Override
    public Topic findById(int topicId) {
        return dslContext.selectFrom(Tables.TOPIC)
                .where(Tables.TOPIC.ID.eq(topicId))
                .fetchOneInto(Topic.class);
    }

    @Override
    public HashMap<Integer, List<Topic>> findAllTopicByListPost(List<Integer> listPostId) {
        HashMap<Integer, List<Topic>> data = new HashMap<>();
        dslContext.transaction(out -> {
            DSLContext context = DSL.using(out);
            listPostId.stream()
                    .forEach(id -> {
                        List<Topic> topicList = context.select(Tables.TOPIC)
                                .from(Tables.TOPIC)
                                .join(Tables.POST_TOPIC)
                                .on(Tables.TOPIC.ID.eq(Tables.POST_TOPIC.TOPIC_ID))
                                .and(Tables.POST_TOPIC.POST_ID.eq(id))
                                .fetchInto(Topic.class);
                        data.put(id, topicList);
                    });
        });
        return data;
    }


    @Override
    public Topic findByName(String name) {
        return dslContext.selectFrom(Tables.TOPIC)
                .where(Tables.TOPIC.TOPIC_NAME.eq(name))
                .fetchOneInto(Topic.class);
    }

    @Override
    public List<Topic> findByPostId(int postId) {

        return dslContext.select(Tables.TOPIC)
                .from(Tables.TOPIC)
                .join(Tables.POST_TOPIC)
                .on(Tables.TOPIC.ID.eq(Tables.POST_TOPIC.TOPIC_ID))
                .and(Tables.POST_TOPIC.POST_ID.eq(postId))
                .fetchInto(Topic.class);
    }

    @Override
    public List<Topic> findAllById(List<Integer> listTopicId) {
        return dslContext.selectFrom(Tables.TOPIC)
                .where(Tables.TOPIC.ID.in(listTopicId))
                .fetchInto(Topic.class);
    }

    @Override
    public void add(Topic topic) {
        dslContext.insertInto(Tables.TOPIC,
                        Tables.TOPIC.TOPIC_NAME,
                        Tables.TOPIC.CREATE_AT,
                        Tables.TOPIC.UPDATE_AT,
                        Tables.TOPIC.DELETE_AT)
                .values(topic.getTopicName(),
                        topic.getCreateAt(),
                        topic.getUpdateAt(),
                        topic.getDeleteAt())
                .execute();
    }

    @Override
    public void update(Topic topic, int id) {
        dslContext.update(Tables.TOPIC)
                .set(Tables.TOPIC.TOPIC_NAME, topic.getTopicName())
                .set(Tables.TOPIC.UPDATE_AT, topic.getUpdateAt())
                .where(Tables.TOPIC.ID.eq(id))
                .execute();
    }

    @Override
    public void delete(int id) {
        dslContext.update(Tables.TOPIC)
                .set(Tables.TOPIC.DELETE_AT, currentDateTime.getCurrentDateTime())
                .where(Tables.TOPIC.ID.eq(id))
                .execute();
    }
}
