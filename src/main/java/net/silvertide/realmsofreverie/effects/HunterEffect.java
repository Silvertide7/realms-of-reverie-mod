package net.silvertide.realmsofreverie.effects;

import net.minecraft.server.level.ServerLevel;
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
            Class<? extends Mob> targetClazz = amplifier >= 2 ? Mob.class : Monster.class;
            int distanceScanned = Math.min(amplifier * 30, 120);
            serverPlayer.serverLevel()
                .getNearbyEntities(targetClazz, TargetingConditions.forCombat(), serverPlayer, AABB.ofSize(serverPlayer.position(), distanceScanned, distanceScanned, distanceScanned))
                .forEach(nearbyEntity -> {
                    nearbyEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 22));
            });
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
