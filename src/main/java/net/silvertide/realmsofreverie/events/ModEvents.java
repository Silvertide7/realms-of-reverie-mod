package net.silvertide.realmsofreverie.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.utils.ParcoolUtils;

@EventBusSubscriber(modid = RealmsOfReverie.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        ParcoolUtils.clearActionAwardsCache();
    }
}
