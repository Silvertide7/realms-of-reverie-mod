package net.silvertide.realmsofreverie.events;

import harmonised.pmmo.api.APIUtils;
import harmonised.pmmo.api.events.XpEvent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.config.ServerConfigs;

@EventBusSubscriber(modid = RealmsOfReverie.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ParcoolEvents {

    @SubscribeEvent(priority=EventPriority.LOW)
    public static void onPmmoSkillUp(XpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        if (ServerConfigs.PARCOOL_LAND_SKILL.get().equals(event.skill) || ServerConfigs.PARCOOL_WATER_SKILL.get().equals(event.skill)) {
            long apiLevel = APIUtils.getLevel(event.skill, serverPlayer);
            RealmsOfReverie.LOGGER.info("[RoR ParCool] XpEvent: skill={}, isLevelUp={}, startLevel={}, endLevel={}, amount={}, apiLevel={}",
                    event.skill, event.isLevelUp(), event.startLevel(), event.endLevel(), event.amountAwarded, apiLevel);
        }
    }
}
