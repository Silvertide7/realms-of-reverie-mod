package net.silvertide.realmsofreverie.events;

import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.silvertide.realmsofreverie.RealmsOfReverie;

@EventBusSubscriber(modid = RealmsOfReverie.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GeneralEvents {

    @SubscribeEvent()
    public static void main(BlockEvent.BreakEvent breakEvent) {
        if(breakEvent.isCanceled()) return;
        if(breakEvent.getPlayer().isCreative()) return;

        BlockEntity blockEntityBroken = breakEvent.getLevel().getBlockEntity(breakEvent.getPos());
        if(blockEntityBroken instanceof Spawner) breakEvent.setCanceled(true);
    }
}
