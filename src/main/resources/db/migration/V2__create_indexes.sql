CREATE INDEX idx_auction_status_ends_at ON auction (status, ends_at);

CREATE INDEX idx_bid_auction_amount ON bid (auction_id, amount DESC);

CREATE INDEX idx_item_owner ON item (owner_id);