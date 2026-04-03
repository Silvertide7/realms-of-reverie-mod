package net.silvertide.realmsofreverie.events;

import harmonised.pmmo.api.APIUtils;
import harmonised.pmmo.api.events.XpEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.config.ServerConfigs;
import net.silvertide.realmsofreverie.utils.ParcoolUtils;

@EventBusSubscriber(modid = RealmsOfReverie.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ParcoolEvents {

    // LOWEST to ensure PMMO has finished loading player data and awarding any scheduled XP.
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
//            ParcoolUtils.refreshLimitations(serverPlayer);
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public static void onPmmoSkillUp(XpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        String landSkill = ServerConfigs.PARCOOL_LAND_SKILL.get();
        String waterSkill = ServerConfigs.PARCOOL_WATER_SKILL.get();

        boolean isRelevantSkill = landSkill.equals(event.skill) || waterSkill.equals(event.skill);

        // Log every XP event for relevant skills
        if (isRelevantSkill) {
            long apiLevel = APIUtils.getLevel(event.skill, serverPlayer);
            long lastLevel = ParcoolUtils.getLastLevel(serverPlayer.getUUID(), event.skill);
            RealmsOfReverie.LOGGER.info("[RoR ParCool] XpEvent: skill={}, isLevelUp={}, startLevel={}, endLevel={}, amount={}, apiLevel={}, lastTracked={}",
                    event.skill, event.isLevelUp(), event.startLevel(), event.endLevel(), event.amountAwarded, apiLevel, lastLevel);
        }

        if (!event.isLevelUp()) return;
        if (!isRelevantSkill) return;

        // Don't trust event.endLevel() — it can report the next level prematurely.
        // Always query the actual current level from PMMO directly.
        long actualLevel = APIUtils.getLevel(event.skill, serverPlayer);

        // Skip if the actual level hasn't changed since last refresh.
        // This avoids redundant network packets from PMMO's isLevelUp() spam.
        if (actualLevel == ParcoolUtils.getLastLevel(serverPlayer.getUUID(), event.skill)) {
            RealmsOfReverie.LOGGER.info("[RoR ParCool]   SKIPPED: actualLevel {} == lastTracked, no refresh needed", actualLevel);
            return;
        }

        RealmsOfReverie.LOGGER.info("[RoR ParCool]   REFRESHING: level changed from {} to {}", ParcoolUtils.getLastLevel(serverPlayer.getUUID(), event.skill), actualLevel);
        ParcoolUtils.setLastLevel(serverPlayer.getUUID(), event.skill, actualLevel);

//        ParcoolUtils.refreshLimitations(serverPlayer);
    }
}
