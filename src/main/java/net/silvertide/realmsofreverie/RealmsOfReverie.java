package net.silvertide.realmsofreverie;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.silvertide.realmsofreverie.registry.EffectRegistry;
import net.silvertide.realmsofreverie.registry.PlacementRegistry;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import java.util.function.Function;

@Mod(RealmsOfReverie.MOD_ID)
public class RealmsOfReverie {
    public static final String MOD_ID = "realmsofreverie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RealmsOfReverie(IEventBus modEventBus) {
        EffectRegistry.register(modEventBus);
        PlacementRegistry.register(modEventBus);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static <T, U> Codec<T> withAlternative(final Codec<T> primary, final Codec<U> alternative, final Function<U, T> converter) {
        return Codec.withAlternative(primary, alternative, converter);
    }
}
