package com.mhsolution.noronapi.data.mapper;

import com.mhsolution.noronapi.data.request.TopicRequest;
import com.mhsolution.noronapi.data.response.TopicResponse;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;
import org.mapstruct.*;

import java.util.List;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;

@Mapper(componentModel = "spring")
public abstract class TopicMapper {

    public abstract Topic toPojo(TopicRequest topicRequest);

    @Named("toResponse")
    public abstract TopicResponse toResponse(Topic topic);

    @IterableMapping(qualifiedByName = "toResponse")
    public abstract List<TopicResponse> toTopicResponses(List<Topic> topicList);

    @AfterMapping
    protected void after(@MappingTarget Topic topic, TopicRequest topicRequest) {
        topic.setCreateAt(getCurrentDateTime());
        topic.setDeleteAt(null);
        topic.setUpdateAt(null);
    }
}
