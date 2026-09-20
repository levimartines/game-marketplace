package dev.levi.gamemarketplace.controllers;

import dev.levi.gamemarketplace.dtos.requests.LoginRequest;
import dev.levi.gamemarketplace.dtos.responses.LoginResponse;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.repositories.PlayerRepository;
import dev.levi.gamemarketplace.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Player player = playerRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), player.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(player.getUsername(), player.getRole().name());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
