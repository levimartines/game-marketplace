package dev.levi.gamemarketplace.services;

import dev.levi.gamemarketplace.entities.RewardClaim;
import dev.levi.gamemarketplace.error.exceptions.RewardOnCooldownException;
import dev.levi.gamemarketplace.entities.Item;
import dev.levi.gamemarketplace.repositories.PlayerRepository;
import dev.levi.gamemarketplace.repositories.RewardClaimRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RewardService {

    private static final Duration COOLDOWN = Duration.ofMinutes(5);
    private static final int MIN_VALUE = 100;
    private static final int MAX_VALUE = 1000;

    private final ItemService itemService;
    private final RewardClaimRepository rewardRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public RewardClaim claimReward(UUID playerId) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player not found: " + playerId));

        var lastClaim = rewardRepository
                .findTopByPlayerIdOrderByClaimedAtDesc(playerId);

        if (lastClaim.isPresent()) {
            var nextAvailableAt = lastClaim.get().getClaimedAt().plus(COOLDOWN);
            if (Instant.now().isBefore(nextAvailableAt)) {
                throw new RewardOnCooldownException(playerId, lastClaim.get().getClaimedAt(), COOLDOWN);
            }
        }

        var random = new Random();
        Item rewardedItem = itemService.generateRandomItem();
        rewardedItem.setOwner(player);
        itemService.save(rewardedItem);

        long goldAmount = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
        player.setGoldBalance(player.getGoldBalance() + goldAmount);
        playerRepository.save(player);

        var claim = new RewardClaim();
        claim.setPlayerId(player.getId());
        claim.setItem(rewardedItem);
        claim.setGoldAmount(goldAmount);
        var now = Instant.now();
        claim.setClaimedAt(now);
        claim.setNextClaimAvailableAt(now.plus(COOLDOWN));
        return rewardRepository.save(claim);
    }

}
