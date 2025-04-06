package net.silvertide.realmsofreverie.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MeditationEffect extends MobEffect {
    private int timeChanneled;
    private Vec3 playerPosition;

    public MeditationEffect(MobEffectCategory category, int color) {
        super(category, color);
        timeChanneled = 0;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        timeChanneled++;
        return timeChanneled > 240 && duration % 20 == 0;
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            timeChanneled = 0;
            playerPosition = serverPlayer.position();
        }
        super.onEffectAdded(livingEntity, amplifier);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            if(!serverPlayer.isCrouching() || !playerPosition.equals(serverPlayer.position())) return false;

            if (serverPlayer.getHealth() < serverPlayer.getMaxHealth()) {
                serverPlayer.heal(Math.max(0.5F * amplifier, 4.0F));
            }
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

//    @Override
//    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {
//        if(livingEntity instanceof ServerPlayer serverPlayer) removeThisEffect(serverPlayer);
//        super.onMobHurt(livingEntity, amplifier, damageSource, amount);
//    }
}
