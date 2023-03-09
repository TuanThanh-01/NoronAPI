package com.mhsolution.noronapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhsolution.noronapi.data.response.TopicResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListTopicResponse {

    @JsonProperty("page_num")
    private int pageNum;

    private int limit;

    private List<TopicResponse> topics;
}
