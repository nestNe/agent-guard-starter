package com.seehold.controller;

import lombok.AllArgsConstructor;
import com.seehold.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/ai")
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping("/chat")
    @PreAuthorize("hasAuthority('agent:chat')")
    public Result<String> chat(@RequestParam("message") String message) {
        log.info("Received message: {}", message);
        String res = chatClient
                .prompt(message)
                .call()
                .content();

        log.info("Result of message: {}", res);
        return Result.success(res);
    }

    @PostMapping("/chat/stream")
    @PreAuthorize("hasAuthority('agent:chat')")
    public Flux<String> chatStream(@RequestParam("message") String message) {
        log.info("Received message: {}", message);
        //todo 流式输出后的bug暂未修复
        return chatClient
                .prompt(message)
                .stream()
                .content();
    }

}
