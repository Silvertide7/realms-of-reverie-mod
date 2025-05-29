package net.silvertide.realmsofreverie.registry;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.placements.AdvancedRandomSpread;

public final class PlacementRegistry {
    /**
     * We are using the Deferred Registry system to register our structure as this is the preferred way on NeoForge.
     * This will handle registering the structure placement type for us at the correct time so we don't have to handle it ourselves.
     */
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENT_TYPES;

    /**
     * Registers the structure placement type  itself and sets what its path is. In this case,
     * this base structure will have the resourcelocation of structure_tutorial:distance_based_structure_placement.
     */
    public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<AdvancedRandomSpread>> ADVANCED_RANDOM_SPREAD;

    public static void register(IEventBus eventBus) {
        STRUCTURE_PLACEMENT_TYPES.register(eventBus);
    }

    /**
     * Originally, I had a double lambda ()->()-> for the RegistryObject line above, but it turns out that
     * some IDEs cannot resolve the typing correctly. This method explicitly states what the return type
     * is so that the IDE can put it into the DeferredRegistry properly.
     */
    private static <T extends StructurePlacement> StructurePlacementType<T> explicitStructureTypeTyping(MapCodec<T> structurePlacementTypeCodec) {
        return () -> structurePlacementTypeCodec;
    }

    static {
        STRUCTURE_PLACEMENT_TYPES = DeferredRegister.create(
                BuiltInRegistries.STRUCTURE_PLACEMENT,
                RealmsOfReverie.MOD_ID
        );

        ADVANCED_RANDOM_SPREAD = STRUCTURE_PLACEMENT_TYPES.register("reverie_advanced_random_spread", () -> explicitStructureTypeTyping(AdvancedRandomSpread.CODEC));
    }
}

