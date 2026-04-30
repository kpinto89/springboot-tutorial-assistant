package com.demo.controller;

import com.demo.model.TutorialRequest;
import com.demo.model.TutorialResponse;
import com.demo.service.TutorialService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/tutorial")
public class TutorialController {

    private final TutorialService tutorialService;

    public TutorialController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    /**
     * POST /api/tutorial/ask
     * Ask any Spring Boot or Java question and receive a full AI answer.
     *
     * Example body:
     * {
     *   "question": "How does dependency injection work in Spring Boot?",
     *   "context": "Spring Boot",
     *   "level": "beginner"
     * }
     */
    @PostMapping("/ask")
    public TutorialResponse ask(@RequestBody TutorialRequest request) {
        return tutorialService.ask(request);
    }

    /**
     * POST /api/tutorial/stream
     * Same as /ask but streams tokens as Server-Sent Events.
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody TutorialRequest request) {
        return tutorialService.stream(request);
    }

    /**
     * GET /api/tutorial/roadmap?topic=Spring+Security&level=intermediate
     * Generates a structured learning roadmap for a given topic.
     */
    @GetMapping("/roadmap")
    public TutorialResponse roadmap(
            @RequestParam String topic,
            @RequestParam(defaultValue = "intermediate") String level) {
        return tutorialService.generateRoadmap(topic, level);
    }

    /**
     * POST /api/tutorial/review
     * Submits code for AI-powered review.
     *
     * Example body:
     * {
     *   "code": "public class MyService { ... }",
     *   "language": "Java"
     * }
     */
    @PostMapping("/review")
    public TutorialResponse review(@RequestBody CodeReviewRequest reviewRequest) {
        return tutorialService.reviewCode(reviewRequest.getCode(), reviewRequest.getLanguage());
    }

    /**
     * GET /api/tutorial/health
     * Simple health-check endpoint.
     */
    @GetMapping("/health")
    public String health() {
        return "Spring Boot AI Tutorial Assistant is running!";
    }

    // Inner DTO for the code review endpoint
    public static class CodeReviewRequest {
        private String code;
        private String language;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
    }
}

