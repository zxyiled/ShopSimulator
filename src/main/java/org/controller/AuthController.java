package org.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthController {

    @GetMapping({"/login", "/dashboard"})
    public String spaRoutes() {
        return "forward:/index.html";
    }

    @GetMapping("/api/me")
    @ResponseBody
    public Map<String, String> currentUser(Authentication authentication) {
        return Map.of("username", authentication.getName());
    }
}
