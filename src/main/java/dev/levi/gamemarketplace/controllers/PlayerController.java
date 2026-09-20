package dev.levi.gamemarketplace.controllers;

import dev.levi.gamemarketplace.dtos.requests.RegisterPlayerRequest;
import dev.levi.gamemarketplace.dtos.responses.PlayerResponse;
import dev.levi.gamemarketplace.mappers.PlayerMapper;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.services.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<PlayerResponse> register(@Valid @RequestBody RegisterPlayerRequest request) {
        Player player = playerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(player));
    }
}