package com.whaa.shop.customerService.controller;

import com.whaa.shop.common.api.ApiResponse;
import com.whaa.shop.customerService.application.CustomerServiceAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/customer-service")
public class CustomerServiceAdminController {
    private final CustomerServiceAnalyticsService analytics;

    public CustomerServiceAdminController(CustomerServiceAnalyticsService analytics) {
        this.analytics = analytics;
    }

    @GetMapping("/analytics")
    ApiResponse<CustomerServiceAnalyticsService.Dashboard> analytics(@RequestParam(defaultValue = "30") int days) {
        return ApiResponse.ok(analytics.dashboard(days));
    }
}
