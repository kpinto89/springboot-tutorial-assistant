package com.demo.controller;

import com.demo.model.TutorialRequest;
import com.demo.model.TutorialResponse;
import com.demo.service.TutorialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UiController {

    private final TutorialService tutorialService;

    public UiController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    /** Home page */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /** Ask page - GET shows form */
    @GetMapping("/ui/ask")
    public String askForm(Model model) {
        model.addAttribute("request", new TutorialRequest());
        return "ask";
    }

    /** Ask page - POST submits question */
    @PostMapping("/ui/ask")
    public String askSubmit(@ModelAttribute TutorialRequest request, Model model) {
        TutorialResponse response = tutorialService.ask(request);
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        return "ask";
    }

    /** Roadmap page - GET shows form */
    @GetMapping("/ui/roadmap")
    public String roadmapForm() {
        return "roadmap";
    }

    /** Roadmap page - GET with params generates roadmap */
    @GetMapping("/ui/roadmap/generate")
    public String roadmapGenerate(
            @RequestParam String topic,
            @RequestParam(defaultValue = "intermediate") String level,
            Model model) {
        TutorialResponse response = tutorialService.generateRoadmap(topic, level);
        model.addAttribute("topic", topic);
        model.addAttribute("level", level);
        model.addAttribute("response", response);
        return "roadmap";
    }

    /** Review page - GET shows form */
    @GetMapping("/ui/review")
    public String reviewForm() {
        return "review";
    }

    /** Review page - POST submits code */
    @PostMapping("/ui/review")
    public String reviewSubmit(
            @RequestParam String code,
            @RequestParam(defaultValue = "Java") String language,
            Model model) {
        TutorialResponse response = tutorialService.reviewCode(code, language);
        model.addAttribute("code", code);
        model.addAttribute("language", language);
        model.addAttribute("response", response);
        return "review";
    }
}

