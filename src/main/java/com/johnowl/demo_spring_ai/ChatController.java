package com.johnowl.demo_spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatModel chatModel, ChatMemory chatMemory, VectorStore vectorStore) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore)
                )
                .build();
    }

    @PostMapping(path = "/chat", consumes = "text/plain", produces = "text/plain")
    public String chat(@RequestBody String message) {
        return chatClient.prompt().user(message).call().content();
    }
}
