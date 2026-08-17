package com.whaa.shop.customerService.controller;

import com.whaa.shop.customerService.application.CustomerServiceChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/customer-service")
public class CustomerServiceController {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceController.class);
    private final CustomerServiceChatService service;

    public CustomerServiceController(CustomerServiceChatService service) {
        this.service = service;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> chat(@RequestBody Question question, Authentication authentication) {
        return service.chat(question.message(), userId(authentication), question.conversationId());
    }

    private Long userId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) return null;
        try { return Long.parseLong(authentication.getName()); }
        catch (NumberFormatException e) {
            log.warn("Customer service authentication subject is not a numeric user id: subject={}", authentication.getName());
            return null;
        }
    }

    public record Question(String message, String conversationId) {}
}
