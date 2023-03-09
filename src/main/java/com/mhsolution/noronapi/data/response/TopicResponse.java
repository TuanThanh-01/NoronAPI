package com.mhsolution.noronapi.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TopicResponse {

    private int id;

    @JsonProperty("name_topic")
    private String topicName;
}
