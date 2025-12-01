package net.silvertide.realmsofreverie.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.commands.CmdRoot;

@EventBusSubscriber(modid=RealmsOfReverie.MOD_ID, bus=EventBusSubscriber.Bus.GAME)
public class SystemEvents {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        CmdRoot.register(event.getDispatcher());
    }
}