package com.whaa.shop.auth.controller;

import com.whaa.shop.auth.application.AuthService;
import com.whaa.shop.auth.application.PasswordResetService;
import com.whaa.shop.common.api.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;
    private final PasswordResetService passwordReset;

    public AuthController(AuthService service, PasswordResetService passwordReset) {
        this.service = service;
        this.passwordReset = passwordReset;
    }

    @PostMapping("/register")
    ApiResponse<AuthService.TokenView> register(@Valid @RequestBody Register r) {
        return ApiResponse.ok(service.register(r.username, r.email, r.password, r.nickname));
    }

    @PostMapping("/login")
    ApiResponse<AuthService.TokenView> login(@Valid @RequestBody Login r) {
        return ApiResponse.ok(service.login(r.username, r.password));
    }

    @PostMapping("/password-reset/code")
    ApiResponse<Void> sendResetCode(@Valid @RequestBody ResetCode r) {
        passwordReset.sendCode(r.username, r.email);
        return ApiResponse.ok();
    }

    @PostMapping("/password-reset")
    ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPassword r) {
        passwordReset.reset(r.username, r.email, r.code, r.password);
        return ApiResponse.ok();
    }

    public record Login(@NotBlank String username, @NotBlank String password) {
    }

    public record Register(@Size(min = 3, max = 30) String username, @Email @NotBlank String email,
                           @Size(min = 6, max = 64) String password,
                           @NotBlank String nickname) {
    }

    public record ResetCode(@Size(min = 3, max = 30) String username, @Email @NotBlank String email) {
    }

    public record ResetPassword(@Size(min = 3, max = 30) String username, @Email @NotBlank String email,
                                @Pattern(regexp = "\\d{6}") String code, @Size(min = 6, max = 64) String password) {
    }
}
