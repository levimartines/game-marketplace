package dev.levi.gamemarketplace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reward_claim")
@Getter
@Setter
public class RewardClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "gold_amount", nullable = false)
    private Long goldAmount;

    @Column(name = "claimed_at", nullable = false, updatable = false)
    private Instant claimedAt;

    @Transient
    private Instant nextClaimAvailableAt;
}
