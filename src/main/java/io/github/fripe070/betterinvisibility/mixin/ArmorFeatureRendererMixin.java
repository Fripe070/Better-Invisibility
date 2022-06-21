package io.github.fripe070.betterinvisibility.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.fripe070.betterinvisibility.Betterinvisibility.CAMOUFLAGE;
import static net.minecraft.entity.effect.StatusEffects.INVISIBILITY;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> {
    private T entity;
    @Shadow
    protected abstract Identifier getArmorTexture(ArmorItem item, boolean legs, @Nullable String overlay);
    private float transparency = 0.2f;

    @Inject(
            method = "renderArmor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorItem;getSlotType()Lnet/minecraft/entity/EquipmentSlot;")
    )
    private void captureContext(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        this.entity = entity;
    }

    @Inject(
            method = "renderArmorParts",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void armorTransparency(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, boolean legs, float red, float green, float blue, String overlay, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            if (player.hasStatusEffect(INVISIBILITY)) {
                VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, RenderLayer.getEntityTranslucent(getArmorTexture(item, legs, overlay)), false, usesSecondLayer);
                model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 0.1f);
                ci.cancel();
            } else if (EnchantmentHelper.getLevel(CAMOUFLAGE, player.getEquippedStack(EquipmentSlot.CHEST)) > 0) {
                transparency = (float) Math.min(1.0f, Math.max(0.16f, Math.max(transparency, player.getVelocity().length() * 2f) - 0.0001f));

                VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, RenderLayer.getEntityTranslucent(getArmorTexture(item, legs, overlay)), false, usesSecondLayer);
                model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, transparency);
                ci.cancel();
            }
        }
    }
}