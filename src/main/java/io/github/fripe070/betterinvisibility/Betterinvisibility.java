package io.github.fripe070.betterinvisibility;

import io.github.fripe070.betterinvisibility.enchantment.CamouflageEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Betterinvisibility implements ModInitializer {
    public static final String MOD_ID = "better-invisibility";

    public static Enchantment CAMOUFLAGE = new CamouflageEnchantment();

    @Override
    public void onInitialize() {
         Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "camouflage"), CAMOUFLAGE);
    }
}
