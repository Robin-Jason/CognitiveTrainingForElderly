package org.example.server.controller;

import org.example.server.dataobject.Story.StoryMessage;
import org.example.server.utils.SignatureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/story")
public class StoryController {

    private final WebClient webClient;
    private final SignatureUtil signatureUtil;

    @Autowired
    public StoryController(WebClient webClient, SignatureUtil signatureUtil) {
        this.webClient = webClient;
        this.signatureUtil = signatureUtil;
    }

    @PostMapping("/start")
    public Mono<ResponseEntity<String>> startStory(@RequestBody StoryMessage message) {
        // 在用户输入内容之前添加指定的文本
        String modifiedContent = "请与我进行故事接龙，故事的开头为：" + message.getContent();
        message.setContent(modifiedContent);

        return handleStoryRequest(message, "/vivogpt/completions");
    }

    @PostMapping("/continue")
    public Mono<ResponseEntity<String>> continueStory(@RequestBody StoryMessage message) {
        return handleStoryRequest(message, "/vivogpt/completions");
    }

    private Mono<ResponseEntity<String>> handleStoryRequest(StoryMessage message, String path) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("requestId", UUID.randomUUID().toString());
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        queryParams.forEach((key, value) -> multiValueMap.add(key, value));

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParams(multiValueMap)
                        .build())
                .headers(headers -> headers.setAll(signatureUtil.generateAuthHeaders("POST", path, queryParams)))
                .bodyValue(buildRequestBody(message))
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> ResponseEntity.ok().body(body))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing your request: " + e.getMessage()));
                });
    }

    private String buildRequestBody(StoryMessage message) {
        return String.format("{\"prompt\": \"%s\", \"model\": \"vivo-BlueLM-TB\", \"sessionId\": \"%s\", \"role\": \"%s\"}",
                message.getContent(),
                message.getSessionId(),
                message.getRole());
    }
}