package dev.levi.gamemarketplace.enums;

import lombok.Getter;

@Getter
public enum ItemType {
    BOW(20),
    SWORD(10),
    AXE(15);

    private final int baseAttack;

    ItemType(int baseAttack) {
        this.baseAttack = baseAttack;
    }

}