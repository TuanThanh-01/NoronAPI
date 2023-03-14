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
import io.reactivex.rxjava3.core.Single;
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

    private Single<HashMap<Integer, Users>> getUsersHashMap(List<Integer> listUserId) {
        HashMap<Integer, Users> dataUser = new HashMap<>();
        return Single.create(singleSubscriber -> {
            List<Users> usersList = userRepository.findAllByListId(listUserId);
            if (usersList.size() == 0) {
                singleSubscriber.onError(new Exception("Can't find user"));
            } else {
                usersList.stream().forEach(users -> dataUser.put(users.getId(), users));
                singleSubscriber.onSuccess(dataUser);
            }
        });
    }

    private Single<List<Comments>> getAllCommentSingleCommentByPostId(int postId) {
        return Single.create(singleSubscriber -> {
            List<Comments> comments = commentRepository.findAllByPostId(postId);
            if (comments.isEmpty()) {
                singleSubscriber.onError(new Exception("Can't find comments"));
            } else {
                singleSubscriber.onSuccess(comments);
            }
        });
    }

    @Override
    public Single<List<CommentResponse>> fetchAllCommentByPostId(int postId) {
        return getAllCommentSingleCommentByPostId(postId)
                .flatMap(comments -> {
                    List<Integer> listUserID = getListUserId(comments);
                    return getUsersHashMap(listUserID)
                            .map(userHashMap -> comments.stream()
                                    .map(comment -> commentMapper.toResponse(comment, userHashMap.get(comment.getUserId())))
                                    .collect(Collectors.toList())
                            );

                });
    }

    private Single<List<Comments>> findAllSingleAnswerComment(int cmtId) {
        return Single.create(singleSubscriber -> {
            List<Comments> comments = commentRepository.findAllAnswerComment(cmtId);
            if(comments.isEmpty()) {
                singleSubscriber.onError(new Exception("Comments can't find"));
            }
            else {
                singleSubscriber.onSuccess(comments);
            }
        });
    }

    @Override
    public Single<List<CommentResponse>> fetchAllAnswerComment(int cmtId) {
        return findAllSingleAnswerComment(cmtId)
                .flatMap(answerComments -> {
                    List<Integer> listUserId = getListUserId(answerComments);
                    return getUsersHashMap(listUserId)
                            .map(userHashMap -> answerComments.stream()
                                    .map(comment -> commentMapper.toResponse(comment, userHashMap.get(comment.getUserId())))
                                    .collect(Collectors.toList()));
                });
    }

    private Single<Users> getUserById(int userId) {
        return Single.create(singleSubscriber -> {
            Users users = userRepository.findByID(userId);
            if(users == null) {
                singleSubscriber.onError(new Exception(""));
            }
            else {
                singleSubscriber.onSuccess(users);
            }
        });
    }

    @Override
    public Single<CommentResponse> createComment(CommentRequest commentRequest) {
        return Single.just(commentMapper.toPojo(commentRequest))
                .flatMap(comment ->  {
                    commentRepository.add(comment);
                    return getUserById(comment.getUserId())
                            .map(users ->  commentMapper.toResponse(comment, users));
                });
    }

    private Single<Comments> getCommentSingleById(int cmtId) {
        return Single.create(singleSubscriber -> {
            Comments comments = commentRepository.findById(cmtId);
            if(comments == null) {
                singleSubscriber.onError(new Exception("Can't find comment"));
            }
            else {
                singleSubscriber.onSuccess(comments);
            }
        });
    }

    @Override
    public Single<CommentResponse> updateComment(int cmtId, CommentRequest commentRequest) {
        return getCommentSingleById(cmtId)
                .flatMap(comment ->  {
                    if (Objects.nonNull(commentRequest.getContent()) && !"".equalsIgnoreCase(commentRequest.getContent())) {
                        comment.setContent(commentRequest.getContent());
                    }
                    comment.setUpdateAt(getCurrentDateTime());
                    commentRepository.update(comment, cmtId);
                    return getUserById(comment.getUserId())
                            .map(user -> commentMapper.toResponse(comment, user));
                });
    }

    @Override
    public void deleteComment(int cmtId) {
        commentRepository.delete(cmtId);
    }
}
