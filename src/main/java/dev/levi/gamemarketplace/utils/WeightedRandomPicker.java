package dev.levi.gamemarketplace.utils;

import java.util.Map;
import java.util.Random;

public class WeightedRandomPicker {

    public static <T> T pick(Map<T, Integer> weights, Random random) {
        int totalWeight = weights.values().stream().mapToInt(Integer::intValue).sum();
        int roll = random.nextInt(totalWeight);
        int cumulative = 0;

        for (Map.Entry<T, Integer> entry : weights.entrySet()) {
            cumulative += entry.getValue();
            if (roll < cumulative) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Weighted pick failed — check weight configuration");
    }
}
