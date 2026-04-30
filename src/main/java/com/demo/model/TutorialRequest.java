package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialRequest {

    /**
     * The user's question or topic to learn about.
     */
    private String question;

    /**
     * Optional: programming language or framework context (e.g., "Java", "Spring Boot").
     */
    private String context;

    /**
     * Optional: skill level of the learner (beginner / intermediate / advanced).
     */
    private String level;
}

