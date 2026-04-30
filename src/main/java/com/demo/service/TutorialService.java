package com.demo.service;

import com.demo.model.TutorialRequest;
import com.demo.model.TutorialResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class TutorialService {

    private static final String SYSTEM_PROMPT_TEMPLATE = """
            You are an expert Spring Boot tutorial assistant.
            Your role is to help developers learn Spring Boot and related technologies.
            
            Guidelines:
            - Provide clear, concise, and accurate explanations.
            - Include practical code examples whenever relevant.
            - Adapt explanations to the learner's skill level: %s.
            - When explaining code, use proper formatting with markdown code blocks.
            - Always mention best practices and common pitfalls.
            - If a context/framework is specified, focus your answer within that scope: %s.
            """;

    private final ChatModel chatModel;
    private final StreamingChatModel streamingChatModel;

    @Value("${spring.ai.openai.chat.options.model:gpt-4o-mini}")
    private String modelName;

    public TutorialService(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        this.chatModel = chatModel;
        this.streamingChatModel = streamingChatModel;
    }

    /**
     * Asks a synchronous tutorial question and returns a full response.
     */
    public TutorialResponse ask(TutorialRequest request) {
        String level = request.getLevel() != null ? request.getLevel() : "intermediate";
        String context = request.getContext() != null ? request.getContext() : "Spring Boot";

        String systemContent = SYSTEM_PROMPT_TEMPLATE.formatted(level, context);

        Prompt prompt = new Prompt(List.of(
                new SystemMessage(systemContent),
                new UserMessage(request.getQuestion())
        ));

        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getContent();

        return new TutorialResponse(answer, request.getQuestion(), modelName);
    }

    /**
     * Streams a tutorial answer token-by-token (Server-Sent Events).
     */
    public Flux<String> stream(TutorialRequest request) {
        String level = request.getLevel() != null ? request.getLevel() : "intermediate";
        String context = request.getContext() != null ? request.getContext() : "Spring Boot";

        String systemContent = SYSTEM_PROMPT_TEMPLATE.formatted(level, context);

        Prompt prompt = new Prompt(List.of(
                new SystemMessage(systemContent),
                new UserMessage(request.getQuestion())
        ));

        return streamingChatModel.stream(prompt)
                .map(chatResponse -> chatResponse.getResult().getOutput().getContent())
                .filter(text -> text != null && !text.isEmpty());
    }

    /**
     * Generates a structured learning roadmap for a given topic.
     */
    public TutorialResponse generateRoadmap(String topic, String level) {
        String systemContent = """
                You are a Spring Boot curriculum expert.
                Generate a structured learning roadmap in markdown format.
                Include: prerequisites, core concepts, hands-on projects, and resources.
                """;

        String userMessage = "Create a detailed learning roadmap for: %s. Target level: %s".formatted(topic, level);

        Prompt prompt = new Prompt(List.of(
                new SystemMessage(systemContent),
                new UserMessage(userMessage)
        ));

        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getContent();

        return new TutorialResponse(answer, "Roadmap for: " + topic, modelName);
    }

    /**
     * Reviews code and returns AI feedback.
     */
    public TutorialResponse reviewCode(String code, String language) {
        String systemContent = """
                You are a senior %s developer and code reviewer.
                Review the provided code for:
                1. Correctness and functionality
                2. Best practices and design patterns
                3. Performance considerations
                4. Security concerns
                5. Suggestions for improvement
                Format your response with clear sections using markdown.
                """.formatted(language != null ? language : "Java/Spring Boot");

        Prompt prompt = new Prompt(List.of(
                new SystemMessage(systemContent),
                new UserMessage("Please review this code:\n\n```\n" + code + "\n```")
        ));

        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getContent();

        return new TutorialResponse(answer, "Code Review", modelName);
    }
}
