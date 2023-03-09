package com.mhsolution.noronapi.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VoteResponse {

    private UserResponse user;

    @JsonProperty("create_at")
    private String createAt;

    @JsonProperty("update_at")
    private String updateAt;

    @JsonProperty("delete_at")
    private String deleteAt;

    @JsonProperty("vote_type")
    private String voteType;

    @JsonProperty("content_id")
    private int contentId;
}
