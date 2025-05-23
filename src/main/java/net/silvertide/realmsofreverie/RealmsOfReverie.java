package net.silvertide.realmsofreverie;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.silvertide.realmsofreverie.registry.EffectRegistry;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.function.Function;

@Mod(RealmsOfReverie.MOD_ID)
public class RealmsOfReverie {
    public static final String MOD_ID = "realmsofreverie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealmsOfReverie(IEventBus modEventBus, ModContainer modContainer) {
        EffectRegistry.register(modEventBus);

//        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC, String.format("%s-server.toml", MOD_ID));
    }

//    public static void init(Path folder) {
//        ConfigHandler.load(folder.resolve("realmsofreverietectonic.json"));
//    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static <T, U> Codec<T> withAlternative(final Codec<T> primary, final Codec<U> alternative, final Function<U, T> converter) {
        return Codec.withAlternative(primary, alternative, converter);
    }
}
