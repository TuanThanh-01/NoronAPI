package com.mhsolution.noronapi.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostRequest {

    private String title;

    private String postType;

    private String content;

    private List<Integer> topicId;

    private int userId;
}
