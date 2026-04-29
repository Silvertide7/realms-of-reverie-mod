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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ParcoolUtils.refreshLimitations(serverPlayer);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPmmoSkillUp(XpEvent event) {
        if (!event.isLevelUp()) return;
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        String landSkill = ServerConfigs.PARCOOL_LAND_SKILL.get();
        String waterSkill = ServerConfigs.PARCOOL_WATER_SKILL.get();

        if (landSkill.equals(event.skill)) {
            ParcoolUtils.refreshLimitations(serverPlayer, event.endLevel(), APIUtils.getLevel(waterSkill, serverPlayer));
        } else if (waterSkill.equals(event.skill)) {
            ParcoolUtils.refreshLimitations(serverPlayer, APIUtils.getLevel(landSkill, serverPlayer), event.endLevel());
        }
    }
}
