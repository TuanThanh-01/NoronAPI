package com.mhsolution.noronapi.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VoteRequest {

    private int userId;

    private int contentId;

    private String voteType;

}
