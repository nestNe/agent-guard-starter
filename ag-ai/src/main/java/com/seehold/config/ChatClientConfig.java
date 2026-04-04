package com.seehold.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                // 核心：设置全局默认系统提示词 (System Prompt)
                .defaultSystem("""
                        你是一个系统管理员，这个系统基于RBAC权限控制。
                        你的职责是：
                        1. 回答关于用户管理、权限分配、角色设置的问题。
                        2. 语气要亲切、耐心，像一位热心的系统管理员。
                        3. 如果用户问的问题与系统管理无关，请礼貌地引导回系统管理话题。
                        """)
                // 设置全局默认模型参数 (如果 yaml 里没配或想覆盖)
                // .defaultOptions(options -> options.temperature(0.7))

                // 添加全局过滤器 (例如：敏感词过滤、日志记录)
                // .filter(new MyCustomFilter())

                .build();
    }
}
