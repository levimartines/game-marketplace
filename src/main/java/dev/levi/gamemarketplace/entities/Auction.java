package dev.levi.gamemarketplace.entities;

import dev.levi.gamemarketplace.enums.AuctionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Player seller;

    @Column(name = "starting_price", nullable = false)
    private Long startingPrice;

    @Column(name = "current_price", nullable = false)
    private Long currentPrice;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status = AuctionStatus.ACTIVE;

    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @Column(name = "winner_id")
    private UUID winner;

    @Version
    private Long version;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    public Auction(Item item, Player seller, Long startingPrice, Instant endsAt) {
        this.item = item;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.currentPrice = 0L;
        this.endsAt = endsAt;
    }
}