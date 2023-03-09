package com.mhsolution.noronapi.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentResponse {

    private String content;

    @JsonProperty("comment_type")
    private String commentType;

    @JsonProperty("parent_id")
    private int parentId;

    @JsonProperty("create_at")
    private String createAt;

    @JsonProperty("update_at")
    private String updateAt;

    private UserResponse user;

    @JsonProperty("num_vote")
    private int numVote;

    @JsonProperty("post_id")
    private int postId;
}
