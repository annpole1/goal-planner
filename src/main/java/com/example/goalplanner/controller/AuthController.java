package com.example.goalplanner.controller;

import com.example.goalplanner.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    private final Map<String, String> users = new ConcurrentHashMap<>();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest req) {
        if (users.containsKey(req.username()))
            return ResponseEntity.badRequest().body("User already exists");

        users.put(req.username(), encoder.encode(req.password()));
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest req) {
        String storedPassword = users.get(req.username());

        if (storedPassword == null || !encoder.matches(req.password(), storedPassword)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Long userId = (long) req.username().hashCode();
        String token = jwtUtil.generateToken(userId);

        return ResponseEntity.ok(Map.of("token", token));
    }

    public record AuthRequest(String username, String password) {}
}