package dev.levi.gamemarketplace.config;

import dev.levi.gamemarketplace.enums.ItemType;
import dev.levi.gamemarketplace.enums.Rarity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "game.drop")
@Getter
@Setter
public class DropProperties {
    private Map<ItemType, Integer> itemWeights;
    private Map<Rarity, Integer> rarityWeights;
}