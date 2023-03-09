package com.mhsolution.noronapi.service.comment;

import com.mhsolution.noronapi.data.mapper.CommentMapper;
import com.mhsolution.noronapi.data.mapper.UserMapper;
import com.mhsolution.noronapi.data.request.CommentRequest;
import com.mhsolution.noronapi.data.response.CommentResponse;
import com.mhsolution.noronapi.repository.comment.CommentRepository;
import com.mhsolution.noronapi.repository.users.UserRepository;
import com.mhsolution.noronapi.service.utils.TimeUtils;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Comments;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CommentMapper commentMapper;


    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              UserMapper userMapper,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
    }

    private List<Integer> getListUserId(List<Comments> comments) {
        return comments.stream()
                .map(Comments::getUserId)
                .collect(Collectors.toList());
    }

    private HashMap<Integer, Users> getUsersHashMap(List<Integer> listUserId) {
        HashMap<Integer, Users> dataUser = new HashMap<>();
        userRepository.findAllByListId(listUserId).stream()
                .forEach(user -> dataUser.put(user.getId(), user));
        return dataUser;
    }

    @Override
    public List<CommentResponse> fetchAllCommentByPostId(int postId) {
        List<Comments> comments = commentRepository.findAllByPostId(postId);
        List<Integer> listUserId = getListUserId(comments);
        HashMap<Integer, Users> dataUser = getUsersHashMap(listUserId);

        return comments.stream()
                .map(comment -> commentMapper.toResponse(comment, dataUser.get(comment.getUserId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> fetchAllAnswerComment(int cmtId) {
        List<Comments> comments = commentRepository.findAllAnswerComment(cmtId);
        List<Integer> listUserId = getListUserId(comments);
        HashMap<Integer, Users> dataUser = getUsersHashMap(listUserId);
        return comments.stream()
                .map(comment -> commentMapper.toResponse(comment, dataUser.get(comment.getUserId())))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comments comment = commentMapper.toPojo(commentRequest);
        commentRepository.add(comment);
        return commentMapper.toResponse(comment, userRepository.findByID(comment.getUserId()));
    }

    @Override
    public CommentResponse updateComment(int cmtId, CommentRequest commentRequest) {
        Comments commentDB = commentRepository.findById(cmtId);
        if (Objects.nonNull(commentRequest.getContent()) && !"".equalsIgnoreCase(commentRequest.getContent())) {
            commentDB.setContent(commentRequest.getContent());
        }
        commentDB.setUpdateAt(getCurrentDateTime());
        commentRepository.update(commentDB, cmtId);
        return commentMapper.toResponse(commentDB, userRepository.findByID(commentDB.getUserId()));
    }

    @Override
    public void deleteComment(int cmtId) {
        commentRepository.delete(cmtId);
    }
}
