package com.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialResponse {

    /** The AI-generated tutorial / explanation. */
    private String answer;

    /** The original question that was asked. */
    private String question;

    /** The model used to generate the response. */
    private String model;
}

