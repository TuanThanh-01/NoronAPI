package com.mhsolution.noronapi.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponse {

    private int id;

    private String username;

    private String avatar;

}
