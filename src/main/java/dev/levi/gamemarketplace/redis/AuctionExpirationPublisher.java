package dev.levi.gamemarketplace.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
public class AuctionExpirationPublisher {
    public static final String AUCTION_EXPIRATION_KEY = "auction:expiration:queue";

    private final StringRedisTemplate redisTemplate;

    public AuctionExpirationPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void scheduleAuctionClose(UUID auctionId, Instant endsAt) {
        long epochMillis = endsAt.toEpochMilli();
        redisTemplate.opsForZSet().add(AUCTION_EXPIRATION_KEY, auctionId.toString(), epochMillis);
        log.info("Scheduled auction {} for closure at {} in Redis ZSET", auctionId, endsAt);
    }
}