package dev.levi.gamemarketplace.controllers;

import dev.levi.gamemarketplace.dtos.responses.RewardClaimResponse;
import dev.levi.gamemarketplace.mappers.RewardClaimMapper;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.services.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final RewardClaimMapper mapper;

    @PostMapping("/claim")
    public ResponseEntity<RewardClaimResponse> claim(@AuthenticationPrincipal Player principal) {
        var claim = rewardService.claimReward(principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(claim));
    }
}
