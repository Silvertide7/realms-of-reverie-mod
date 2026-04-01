package net.silvertide.realmsofreverie;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.silvertide.realmsofreverie.config.ServerConfigs;
import net.silvertide.realmsofreverie.registry.EffectRegistry;
import net.silvertide.realmsofreverie.registry.PlacementRegistry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;


@Mod(RealmsOfReverie.MOD_ID)
public class RealmsOfReverie {
    public static final String MOD_ID = "realmsofreverie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealmsOfReverie(IEventBus modEventBus, ModContainer modContainer) {
        EffectRegistry.register(modEventBus);
        PlacementRegistry.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfigs.SPEC, String.format("%s-server.toml", RealmsOfReverie.MOD_ID));
    }
}
