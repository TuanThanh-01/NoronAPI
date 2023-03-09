package com.mhsolution.noronapi.data.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentRequest {

    private String content;

    private String commentType;

    private int parentId;

    private int userId;

    private int postId;
}
