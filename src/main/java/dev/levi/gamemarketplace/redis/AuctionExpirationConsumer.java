package dev.levi.gamemarketplace.redis;

import dev.levi.gamemarketplace.services.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
public class AuctionExpirationConsumer {

    private final StringRedisTemplate redisTemplate;
    private final AuctionService auctionService;

    public AuctionExpirationConsumer(StringRedisTemplate redisTemplate, AuctionService auctionService) {
        this.redisTemplate = redisTemplate;
        this.auctionService = auctionService;
    }

    @Scheduled(fixedRate = 1000)
    public void processExpiredAuctions() {
        long now = System.currentTimeMillis();

        Set<String> expiredAuctionIds = redisTemplate.opsForZSet()
                .rangeByScore(AuctionExpirationPublisher.AUCTION_EXPIRATION_KEY, 0, now);
        if (expiredAuctionIds == null || expiredAuctionIds.isEmpty()) {
            return;
        }

        for (String auctionIdStr : expiredAuctionIds) {
            // Remove from Redis to avoid duplicit between workers
            Long removed = redisTemplate.opsForZSet()
                    .remove(AuctionExpirationPublisher.AUCTION_EXPIRATION_KEY, auctionIdStr);

            if (removed != null && removed > 0) {
                UUID auctionId = UUID.fromString(auctionIdStr);
                log.info("Triggering real-time closure for auction: {}", auctionId);

                try {
                    auctionService.closeAuction(auctionId);
                } catch (Exception e) {
                    log.error("Failed to close expired auction: {}", auctionId, e);
                }
            }
        }
    }
}
