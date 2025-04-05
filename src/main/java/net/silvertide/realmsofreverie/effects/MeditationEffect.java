package net.silvertide.realmsofreverie.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MeditationEffect extends MobEffect {
    private int timeChanneled;
    protected MeditationEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
        timeChanneled = 0;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return timeChanneled > 60 && duration % 10 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            if(amplifier >= 1) {
                if (serverPlayer.getHealth() < serverPlayer.getMaxHealth()) {
                    serverPlayer.heal(Math.max(1.0F * amplifier, 4.0F));
                }
            }
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {
        super.onMobHurt(livingEntity, amplifier, damageSource, amount);
    }
}
