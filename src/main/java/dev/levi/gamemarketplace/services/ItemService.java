package dev.levi.gamemarketplace.services;

import dev.levi.gamemarketplace.config.DropProperties;
import dev.levi.gamemarketplace.entities.Item;
import dev.levi.gamemarketplace.entities.Player;
import dev.levi.gamemarketplace.repositories.ItemRepository;
import dev.levi.gamemarketplace.utils.WeightedRandomPicker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final DropProperties dropProperties;

    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> findByOwnerId(Player player)  {
        return itemRepository.findByOwnerId(player.getId());
    }

    public Item generateRandomItem() {
        var random = new Random();
        var type = WeightedRandomPicker.pick(dropProperties.getItemWeights(), random);
        var rarity = WeightedRandomPicker.pick(dropProperties.getRarityWeights(), random);

        var item = new Item();
        item.setType(type);
        item.setAttackPower(type.getBaseAttack());
        item.setRarity(rarity);

        return itemRepository.save(item);
    }
}
