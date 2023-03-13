package com.mhsolution.noronapi.service.post;

import com.mhsolution.noronapi.data.mapper.PostMapper;
import com.mhsolution.noronapi.data.mapper.TopicMapper;
import com.mhsolution.noronapi.data.mapper.UserMapper;
import com.mhsolution.noronapi.data.request.PostRequest;
import com.mhsolution.noronapi.data.response.PostResponse;
import com.mhsolution.noronapi.entity.ListPostResponse;
import com.mhsolution.noronapi.repository.post.PostRepository;
import com.mhsolution.noronapi.repository.topic.TopicRepository;
import com.mhsolution.noronapi.repository.users.UserRepository;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Post;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Topic;
import com.tej.JooQDemo.jooq.sample.model.tables.pojos.Users;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
        List<Post> postList = postRepository.findAll(pageNum, limit);

//        Single singlePostList = Single.create(singleSubscriber -> {
//            singleSubscriber.onSuccess(postList);
//        });
        Single singleMapUser = getUsersMap(postList);
        Single singlePostTopicMap = getPostTopicMap(postList);

        return Single.zip(singleMapUser, singlePostTopicMap, (o1, o2) -> {
            List<PostResponse> responses = postMapper.toPostResponse(postList, (Map<Integer, List<Topic>>) o2, (Map<Integer, Users>) o1);
            return Single.just(new ListPostResponse()
                    .setPageNum(pageNum)
                    .setLimit(limit)
                    .setPosts(responses));
        });

//        Single single1 = Single.just(postList)
//                .flatMap(list ->
//                    getUsersMap(list)
//                            .map(integerUsersMap -> {
//
//                            })
//                );


//          List<PostResponse> response = postMapper.toPostResponse(postList, postTopicMap, userMap);
//        Map<Integer, Users> userMap = getUsersMap(postList);
//        HashMap<Integer, List<Topic>> postTopicMap = getPostTopicMap(postList);
//        return Single.just(new ListPostResponse()
//                .setPageNum(pageNum)
//                .setLimit(limit)
//                .setPosts(response));
//        Single<Map<Integer, Users>> userMap = getUsersMap(postList);
//        return null;
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
                        singleSubscriber.onSuccess(users.stream().collect(toMap(Users::getId, Function.identity())));
                    }
                }
        );
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

    @Override
    public Single<PostResponse> findPostById(int postId) {
        Post post = postRepository.findById(postId);
        Users user = userRepository.findByID(post.getUserId());
        List<Topic> topicList = topicRepository.findByPostId(postId);
//        return postMapper.toPostResponse(post, user, topicList);
        return null;
    }


    @Override
    public Single<PostResponse> createPost(PostRequest postRequest) {
        Post post = postMapper.toPojo(postRequest);
        Users users = userRepository.findByID(postRequest.getUserId());
        List<Topic> topicList = topicRepository.findAllById(postRequest.getTopicId());
        postRepository.add(post, postRequest.getTopicId());
//        return postMapper.toPostResponse(post, users, topicList);
        return null;
    }


    @Override
    public Single<PostResponse> updatePost(int postId, PostRequest postRequest) {
        Post postDB = postRepository.findById(postId);
        if (Objects.nonNull(postRequest.getTitle()) && !"".equalsIgnoreCase(postRequest.getTitle())) {
            postDB.setTitle(postRequest.getTitle());
        }
        if (Objects.nonNull(postRequest.getContent()) && !"".equalsIgnoreCase(postRequest.getContent())) {
            postDB.setContent(postRequest.getContent());
        }
        postDB.setUpdateAt(getCurrentDateTime());
        postRepository.update(postDB, postId);
        Users users = userRepository.findByID(postDB.getUserId());
        List<Topic> topicList = topicRepository.findByPostId(postDB.getId());
//        return postMapper.toPostResponse(postDB, users, topicList);
        return null;
    }


    @Override
    public void deletePost(int postId) {
        postRepository.delete(postId);
    }
}
