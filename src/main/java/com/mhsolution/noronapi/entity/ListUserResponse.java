package com.mhsolution.noronapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhsolution.noronapi.data.response.UserResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListUserResponse {

    @JsonProperty("page_num")
    private int pageNum;

    private int limit;

    private List<UserResponse> users;
}
