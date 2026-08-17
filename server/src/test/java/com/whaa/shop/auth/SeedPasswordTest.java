package com.whaa.shop.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SeedPasswordTest {
    @Test
    void demoSeedHashMatchesDocumentedPassword() {
        String hash = "$2a$10$0nYazucnGAq59EP8MuOhwe7RH4T0aWu5CNDFc6GrNrd/UGtlEKdoa";
        assertTrue(new BCryptPasswordEncoder().matches("password", hash));
    }
}
