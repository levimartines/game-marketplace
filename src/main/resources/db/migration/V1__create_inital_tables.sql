CREATE TABLE player
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    gold_balance  BIGINT       NOT NULL DEFAULT 0,
    created_at    TIMESTAMP    NOT NULL
);

CREATE TABLE item
(
    id           UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    owner_id     UUID REFERENCES player (id),
    type         VARCHAR(20) NOT NULL,
    attack_power INTEGER     NOT NULL,
    rarity       VARCHAR(20) NOT NULL,
    created_at   TIMESTAMP   NOT NULL
);

CREATE TABLE auction
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    item_id        UUID        NOT NULL REFERENCES item (id),
    seller_id      UUID        NOT NULL REFERENCES player (id),
    starting_price BIGINT      NOT NULL,
    current_price  BIGINT      NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, CLOSED, CANCELLED
    ends_at        TIMESTAMP   NOT NULL,
    winner_id      UUID REFERENCES player (id),
    version        BIGINT      NOT NULL DEFAULT 0,
    created_at     TIMESTAMP   NOT NULL
);
CREATE UNIQUE INDEX idx_unique_active_auction_per_item
    ON auction (item_id)
    WHERE status = 'ACTIVE';

CREATE TABLE bid
(
    id         UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    auction_id UUID      NOT NULL REFERENCES auction (id),
    bidder_id  UUID      NOT NULL REFERENCES player (id),
    amount     BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_bid_auction ON bid (auction_id);

CREATE TABLE reward_claim
(
    id          UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    player_id   UUID      NOT NULL REFERENCES player (id),
    item_id     UUID REFERENCES item (id),
    gold_amount BIGINT    NOT NULL,
    claimed_at  TIMESTAMP NOT NULL
);
CREATE INDEX idx_reward_claim_player_time ON reward_claim (player_id, claimed_at);
