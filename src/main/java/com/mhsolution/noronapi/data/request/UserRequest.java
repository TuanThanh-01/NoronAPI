package com.mhsolution.noronapi.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequest {

    private String username;

    private String email;

    private String phoneNumber;

    private String pass;
}
