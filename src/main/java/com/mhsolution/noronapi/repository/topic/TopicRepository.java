package com.mhsolution.noronapi.repository.topic;

import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;

import java.util.HashMap;
import java.util.List;

public interface TopicRepository {

    List<Topic> findAll(int pageNum, int limit);

    Topic findById(int topicId);

    HashMap<Integer, List<Topic>> findAllTopicByListPost(List<Integer> listPostId);

    Topic findByName(String name);

    List<Topic> findByPostId(int postId);

    List<Topic> findAllById(List<Integer> listTopicId);

    void add(Topic topic);

    void update(Topic topic, int id);

    void delete(int id);
}
