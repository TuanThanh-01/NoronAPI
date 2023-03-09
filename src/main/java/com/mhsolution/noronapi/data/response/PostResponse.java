package com.mhsolution.noronapi.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostResponse {

    String title;

    @JsonProperty("post_type")
    String postType;

    String content;

    @JsonProperty("num_view")
    int numView;

    List<TopicResponse> topics;

    UserResponse user;

    @JsonProperty("num_vote")
    int numVote;

    @JsonProperty("num_comment")
    int numComment;

    @JsonProperty("create_at")
    private String createAt;

    @JsonProperty("update_at")
    private String updateAt;
}
