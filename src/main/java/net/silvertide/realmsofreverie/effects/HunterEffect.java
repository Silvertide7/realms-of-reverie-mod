package net.silvertide.realmsofreverie.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

public class HunterEffect extends MobEffect {
    public HunterEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            Class<? extends Mob> targetClazz = amplifier > 1 ? Mob.class : Monster.class;
            int distanceScanned = 25 + Math.min(amplifier * 20, 120);
            serverPlayer.serverLevel()
                .getNearbyEntities(targetClazz, TargetingConditions.forCombat(), serverPlayer, AABB.ofSize(serverPlayer.position(), distanceScanned, distanceScanned, distanceScanned))
                .forEach(nearbyEntity -> {
                    nearbyEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 30));
            });
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % Math.max(120 / amplifier, 20) == 0;
    }
}
