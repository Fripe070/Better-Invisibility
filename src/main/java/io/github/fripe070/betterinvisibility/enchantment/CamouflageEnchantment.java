package io.github.fripe070.betterinvisibility.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class CamouflageEnchantment extends Enchantment {
    public CamouflageEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, new EquipmentSlot[] {EquipmentSlot.CHEST});
    }

    @Override
    public int getMinPower(int level) {
        return 26;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

}