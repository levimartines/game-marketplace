package dev.levi.gamemarketplace.dtos.responses;

import dev.levi.gamemarketplace.enums.ItemType;
import dev.levi.gamemarketplace.enums.Rarity;

import java.util.UUID;

public record ItemResponse(UUID id, ItemType type, Integer attackPower, Rarity rarity) {
}
