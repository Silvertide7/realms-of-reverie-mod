package net.silvertide.realmsofreverie;

import net.silvertide.realmsofreverie.registry.EffectRegistry;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RealmsOfReverie.MOD_ID)
public class RealmsOfReverie {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "realmsofreverie";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public RealmsOfReverie(IEventBus modEventBus, ModContainer modContainer)
    {
        EffectRegistry.register(modEventBus);
    }
}
