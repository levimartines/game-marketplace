package dev.levi.gamemarketplace.repositories;

import dev.levi.gamemarketplace.entities.RewardClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RewardClaimRepository extends JpaRepository<RewardClaim, UUID> {
    Optional<RewardClaim> findTopByPlayerIdOrderByClaimedAtDesc(UUID playerId);
}