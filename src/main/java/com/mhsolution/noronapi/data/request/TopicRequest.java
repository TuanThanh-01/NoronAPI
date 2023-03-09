package com.mhsolution.noronapi.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TopicRequest {

    public String topicName;
}
