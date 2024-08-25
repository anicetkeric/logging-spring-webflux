package com.bootlabs.logging.controller;

import com.bootlabs.logging.domain.ApiResponse;
import com.bootlabs.logging.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/trace")
public class TraceController {

    public static final String JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com";

    private final WebClient webClient;



    @GetMapping(value = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse> getLogging() {
        //Call webclient
        var postResponse = createPost();

        return postResponse.flatMap(pt -> Mono.just(new ApiResponse(pt, "response")));
    }

    private Mono<Post> createPost() {
        var post = new Post("foo", "bar", 1);

        return webClient.post()
                .uri(MessageFormat.format("{0}/posts", JSON_PLACEHOLDER_BASE_URL))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(Mono.just(post), Post.class)
                .retrieve()
                .bodyToMono(Post.class);
    }
}
