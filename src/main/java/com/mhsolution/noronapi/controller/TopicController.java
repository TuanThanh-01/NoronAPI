package com.mhsolution.noronapi.controller;

import com.mhsolution.noronapi.data.request.TopicRequest;
import com.mhsolution.noronapi.data.response.TopicResponse;
import com.mhsolution.noronapi.entity.ListTopicResponse;
import com.mhsolution.noronapi.service.topic.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/topic/all")
    public ResponseEntity<ListTopicResponse> getAllTopic(@RequestParam int limit, @RequestParam int pageNum) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.fetchAllTopic(pageNum, limit));
    }

    @PostMapping("/topic/create")
    public ResponseEntity<TopicResponse> createTopic(@RequestBody TopicRequest topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createTopic(topic));
    }

    @PutMapping("/topic/update/{id}")
    public ResponseEntity<TopicResponse> updateTopic(@PathVariable("id") int id, @RequestBody TopicRequest topic) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.updateTopic(id, topic));
    }

    @DeleteMapping("/topic/delete/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable("id") int id) {
        topicService.deleteTopic(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Topic Success");
    }
}
