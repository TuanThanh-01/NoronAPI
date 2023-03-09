package com.mhsolution.noronapi.service.topic;

import com.mhsolution.noronapi.data.request.TopicRequest;
import com.mhsolution.noronapi.data.response.TopicResponse;
import com.mhsolution.noronapi.entity.ListTopicResponse;

import java.util.List;

public interface TopicService {

    ListTopicResponse fetchAllTopic(int pageNum, int limit);

    TopicResponse createTopic(TopicRequest topicRequest);

    TopicResponse updateTopic(int id, TopicRequest topic);

    void deleteTopic(int id);
}
