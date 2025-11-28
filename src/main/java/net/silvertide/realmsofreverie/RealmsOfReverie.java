package net.silvertide.realmsofreverie;

import net.silvertide.realmsofreverie.registry.EffectRegistry;
import net.silvertide.realmsofreverie.registry.PlacementRegistry;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;


@Mod(RealmsOfReverie.MOD_ID)
public class RealmsOfReverie {
    public static final String MOD_ID = "realmsofreverie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealmsOfReverie(IEventBus modEventBus) {
        EffectRegistry.register(modEventBus);
        PlacementRegistry.register(modEventBus);
    }
}
