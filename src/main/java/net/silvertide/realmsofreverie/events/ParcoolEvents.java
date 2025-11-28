package net.silvertide.realmsofreverie.events;

import com.alrex.parcool.api.unstable.Limitation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.helpers.ParcoolUtils;

@EventBusSubscriber(modid = RealmsOfReverie.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ParcoolEvents {
    @SubscribeEvent(priority= EventPriority.LOW)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {

            Limitation playerLimitation = Limitation.getIndividual(serverPlayer);
            if(!playerLimitation.isEnabled()) {
                playerLimitation.enable();
            }

            ParcoolUtils.disableAllLimitations(playerLimitation);
            ParcoolUtils.updateParcoolLimitations(serverPlayer, playerLimitation);
        }
    }
}
