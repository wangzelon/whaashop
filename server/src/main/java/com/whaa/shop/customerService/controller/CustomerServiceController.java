package com.whaa.shop.customerService.controller;

import com.whaa.shop.customerService.application.CustomerServiceChatService;
import com.whaa.shop.common.security.CurrentUser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/customer-service")
public class CustomerServiceController {
    private final CustomerServiceChatService service;

    public CustomerServiceController(CustomerServiceChatService service) {
        this.service = service;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> chat(@RequestBody Question question) {
        return service.chat(question.message(), CurrentUser.id(), question.conversationId());
    }

    public record Question(String message, String conversationId) {}
}
