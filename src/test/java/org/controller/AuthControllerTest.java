package org.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("AuthController")
class AuthControllerTest {

    private AuthController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthController();
    }

    @Test
    @DisplayName("spaRoutes forwards to index.html")
    void spaRoutes() {
        String view = controller.spaRoutes();
        assertEquals("forward:/index.html", view);
    }

    @Test
    @DisplayName("currentUser returns username from Authentication")
    void currentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");
        Map<String, String> result = controller.currentUser(auth);
        assertEquals("admin", result.get("username"));
    }
}
