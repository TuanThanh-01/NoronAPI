package com.mhsolution.noronapi.service.topic;

import com.mhsolution.noronapi.data.mapper.TopicMapper;
import com.mhsolution.noronapi.data.request.TopicRequest;
import com.mhsolution.noronapi.data.response.TopicResponse;
import com.mhsolution.noronapi.entity.ListPostResponse;
import com.mhsolution.noronapi.entity.ListTopicResponse;
import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.mhsolution.noronapi.repository.topic.TopicRepository;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;
import org.springframework.stereotype.Service;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;

import java.util.List;
import java.util.Objects;


@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;


    public TopicServiceImpl(TopicRepository topicRepository,
                            TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    @Override
    public ListTopicResponse fetchAllTopic(int pageNum, int limit) {
        ListTopicResponse listTopicResponse = new ListTopicResponse();
        return listTopicResponse.setLimit(limit)
                .setPageNum(pageNum)
                .setTopics(topicMapper.toTopicResponses(topicRepository.findAll(pageNum, limit)));
    }

    @Override
    public TopicResponse createTopic(TopicRequest topicRequest) {
        Topic topic = topicMapper.toPojo(topicRequest);
        topicRepository.add(topic);
        return topicMapper.toResponse(topic);
    }


    @Override
    public TopicResponse updateTopic(int topicId, TopicRequest newTopic) {
        Topic topicDB = topicRepository.findById(topicId);
        if (Objects.nonNull(newTopic.getTopicName()) && !"".equalsIgnoreCase(newTopic.getTopicName())) {
            topicDB.setTopicName(newTopic.getTopicName());
        }
        topicDB.setUpdateAt(getCurrentDateTime());
        topicRepository.update(topicDB, topicId);
        return topicMapper.toResponse(topicDB);
    }

    @Override
    public void deleteTopic(int topicId) {
        topicRepository.delete(topicId);
    }
}
