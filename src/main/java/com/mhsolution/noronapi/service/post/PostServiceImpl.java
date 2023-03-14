package com.mhsolution.noronapi.service.post;

import com.mhsolution.noronapi.data.mapper.PostMapper;
import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.response.PostResponse;
import com.mhsolution.noronapi.entity.ListPostResponse;
import com.mhsolution.noronapi.repository.post.PostRepository;
import com.mhsolution.noronapi.repository.topic.TopicRepository;
import com.mhsolution.noronapi.repository.users.UserRepository;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Post;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mhsolution.noronapi.service.utils.TimeUtils.getCurrentDateTime;
import static java.util.stream.Collectors.toMap;

@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;


    public PostServiceImpl(PostMapper postMapper,
                           PostRepository postRepository,
                           TopicRepository topicRepository,
                           UserRepository userRepository) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Single<ListPostResponse> findAll(int pageNum, int limit) {
        return Single.just("io")
                .flatMap(s -> getAllPost(pageNum, limit))
                .flatMap(posts -> Single.zip(
                        getUsersMap(posts),
                        getPostTopicMap(posts),
                        (userMap, postMap) -> {
                            List<PostResponse> responses = postMapper.toPostResponse(posts, postMap, userMap);
                            return new ListPostResponse()
                                    .setPageNum(pageNum)
                                    .setLimit(limit)
                                    .setPosts(responses);
                        }
                ));
    }

    private Single<List<Post>> getAllPost(int pageNum, int limit) {
        return Single.create(singleSubscriber -> {
            List<Post> postList = postRepository.findAll(pageNum, limit);
            if (postList.size() == 0) {
                singleSubscriber.onError(new Exception("Can't find user"));
            } else {
                singleSubscriber.onSuccess(postList);
            }
        });
    }

    private Single<Map<Integer, Users>> getUsersMap(List<Post> postList) {
        List<Integer> listUserId = postList.stream()
                .map(Post::getUserId)
                .collect(Collectors.toList());
        return Single.create(singleSubscriber -> {
            List<Users> users = userRepository.findAllByListId(listUserId);
            if (users.size() == 0) {
                singleSubscriber.onError(new Exception("Can't find user"));
            } else {
                singleSubscriber.onSuccess(users.stream()
                        .collect(toMap(Users::getId, Function.identity())));
            }
        });
    }

    private Single<HashMap<Integer, List<Topic>>> getPostTopicMap(List<Post> postList) {
        List<Integer> listPostId = postList.stream()
                .map(Post::getId)
                .collect(Collectors.toList());
        return Single.create(singleSubscriber -> {
            HashMap<Integer, List<Topic>> mapPostTopic = topicRepository.findAllTopicByListPost(listPostId);
            if (mapPostTopic.isEmpty()) {
                singleSubscriber.onError(new Exception("Can't find user"));
            } else {
                singleSubscriber.onSuccess(mapPostTopic);
            }
        });
    }

    private Single<Post> findPostSingleById(int postId) {
        return Single.create(singleSubscriber -> {
            Post post = postRepository.findById(postId);
            if (post == null) {
                singleSubscriber.onError(new Exception("Post can't find"));
            } else {
                singleSubscriber.onSuccess(post);
            }
        });
    }

    private Single<Users> findUserSingleById(int userId) {
        return Single.create(singleSubscriber -> {
            Users users = userRepository.findByID(userId);
            if (users == null) {
                singleSubscriber.onError(new Exception("Users can't find"));
            } else {
                singleSubscriber.onSuccess(users);
            }
        });
    }

    private Single<List<Topic>> findAllTopicSingleByPostId(int postId) {
        return Single.create(singleSubscriber -> {
            List<Topic> topicList = topicRepository.findByPostId(postId);
            if (topicList.size() == 0) {
                singleSubscriber.onError(new Exception("Topics can't find"));
            } else {
                singleSubscriber.onSuccess(topicList);
            }
        });
    }

    @Override
    public Single<PostResponse> findPostById(int postId) {
        return Single.just("io")
                .flatMap(s -> findPostSingleById(postId))
                .flatMap(post -> Single.zip(
                        findUserSingleById(post.getUserId()),
                        findAllTopicSingleByPostId(postId),
                        (user, topicList) -> postMapper.toPostResponse(post, user, topicList)
                ));
    }

    private Single<List<Topic>> findAllTopicSingleById(List<Integer> topicListId) {
        return Single.create(singleSubscriber -> {
            List<Topic> topicList = topicRepository.findAllById(topicListId);
            if (topicList.size() == 0) {
                singleSubscriber.onError(new Exception("Can't find list topic"));
            } else {
                singleSubscriber.onSuccess(topicList);
            }
        });
    }


    @Override
    public Single<PostResponse> createPost(PostRequest postRequest) {
        return Single.just(postMapper.toPojo(postRequest))
                .flatMap(post -> Single.zip(
                        findUserSingleById(post.getUserId()),
                        findAllTopicSingleById(postRequest.getTopicId()),
                        (user, listPostId) -> {
                            postRepository.add(post, postRequest.getTopicId());
                            return postMapper.toPostResponse(post, user, listPostId);
                        }
                ));
    }


    @Override
    public Single<PostResponse> updatePost(int postId, PostRequest postRequest) {
        return Single.just("io")
                .flatMap(s -> findPostSingleById(postId))
                .flatMap(post -> Single.zip(
                        findUserSingleById(post.getUserId()),
                        findAllTopicSingleByPostId(postId),
                        (user, listTopic) -> {
                            if (Objects.nonNull(postRequest.getTitle()) && !"".equalsIgnoreCase(postRequest.getTitle())) {
                                post.setTitle(postRequest.getTitle());
                            }
                            if (Objects.nonNull(postRequest.getContent()) && !"".equalsIgnoreCase(postRequest.getContent())) {
                                post.setContent(postRequest.getContent());
                            }
                            post.setUpdateAt(getCurrentDateTime());
                            postRepository.update(post, postId);
                            return postMapper.toPostResponse(post, user, listTopic);
                        }
                ));
    }


    @Override
    public void deletePost(int postId) {
        postRepository.delete(postId);
    }
}
