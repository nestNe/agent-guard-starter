package com.seehold.config;

import com.seehold.constant.PromptConstant;
import com.seehold.tools.UserTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient userManageClient(OpenAiChatModel model, UserTools userTools) {
        return ChatClient.builder(model)
                .defaultSystem(PromptConstant.USER_MANAGE_PROMPT)
                .defaultTools(userTools)
                .build();
    }
}
