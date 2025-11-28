package net.silvertide.realmsofreverie.events;

import harmonised.pmmo.api.events.XpEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.utils.ParcoolUtils;

@EventBusSubscriber(modid = RealmsOfReverie.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ParcoolEvents {
    @SubscribeEvent(priority= EventPriority.LOW)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            ParcoolUtils.refreshLimitations(serverPlayer);
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public static void onAcrobaticsSkillUp(XpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        if (!ParcoolUtils.PARCOOL_SKILL.equals(event.skill)) return;
        if (!event.isLevelUp()) return;

        ParcoolUtils.refreshLimitations(serverPlayer, event.endLevel());
    }
}
