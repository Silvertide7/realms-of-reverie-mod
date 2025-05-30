package net.silvertide.realmsofreverie.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.silvertide.realmsofreverie.registry.PlacementRegistry;
import org.jetbrains.annotations.NotNull;


// MOST OF THIS CODE WAS ADAPTED FROM INTEGRATED API's IMPLEMENTATION: https://www.curseforge.com/minecraft/mc-mods/integrated-api

public class AdvancedRandomSpread extends RandomSpreadStructurePlacement {

    public static final MapCodec<AdvancedRandomSpread> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(AdvancedRandomSpread::locateOffset),
            StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(AdvancedRandomSpread::frequencyReductionMethod),
            Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(AdvancedRandomSpread::frequency),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(AdvancedRandomSpread::salt),
            StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(AdvancedRandomSpread::exclusionZone),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spacing").forGetter(AdvancedRandomSpread::spacing),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("separation").forGetter(AdvancedRandomSpread::separation),
            RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(AdvancedRandomSpread::spreadType),
            AdvancedRandomSpread.SuperExclusionZone.CODEC.optionalFieldOf("super_exclusion_zone").forGetter(AdvancedRandomSpread::superExclusionZone),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("min_distance_from_world_origin").forGetter(AdvancedRandomSpread::minDistanceFromWorldOrigin)
    ).apply(instance, instance.stable(AdvancedRandomSpread::new)));

    private final Optional<Integer> minDistanceFromWorldOrigin;
    private final Optional<SuperExclusionZone> superExclusionZone;

    public AdvancedRandomSpread(Vec3i locationOffset,
                                   StructurePlacement.FrequencyReductionMethod frequencyReductionMethod,
                                   float frequency,
                                   int salt,
                                   Optional<ExclusionZone> exclusionZone,
                                   int spacing,
                                   int separation,
                                   RandomSpreadType spreadType,
                                   Optional<SuperExclusionZone> superExclusionZone,
                                   Optional<Integer> minDistanceFromWorldOrigin
    ) {
        super(locationOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
        this.minDistanceFromWorldOrigin = minDistanceFromWorldOrigin;
        this.superExclusionZone = superExclusionZone;

        // Helpful validation to ensure that spacing value is always greater than separation value
        if (spacing <= separation) {
            throw new RuntimeException("""
                Spacing cannot be less than separation.
                Please correct this error as there's no way to spawn this structure properly
                    Spacing: %s
                    Separation: %s.
            """.formatted(spacing, separation));
        }
    }

    public Optional<Integer> minDistanceFromWorldOrigin() {
        return this.minDistanceFromWorldOrigin;
    }

    public Optional<SuperExclusionZone> superExclusionZone() { return this.superExclusionZone; }

    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return super.isStructureChunk(structureState, x, z) &&
                (this.superExclusionZone.isEmpty() || !(this.superExclusionZone.get()).isPlacementForbidden(structureState, x, z));
    }

    @Override
    public ChunkPos getPotentialStructureChunk(long seed, int regionX, int regionZ) {
        return super.getPotentialStructureChunk(seed, regionX, regionZ);
    }

    // Override this method to add coordinate checking.
    // The x and z here is in chunk positions.
    // What we do is we check if the structure is too close to world center and if so, return false.
    // Otherwise, if far enough away, run the normal structure position choosing code.
    // When this returns true, the structure's type class will be called to see if the structure layout can be made.
    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int z) {
        if (minDistanceFromWorldOrigin.isPresent()) {
            // Convert chunk position to block position.
            long xBlockPos = x * 16L;
            long zBlockPos = z * 16L;

            // Simple fast distance check without needing to do a square root. The threshold is circular around world origin.
            if ((xBlockPos * xBlockPos) + (zBlockPos * zBlockPos) < (((long) minDistanceFromWorldOrigin.get()) * minDistanceFromWorldOrigin.get())) {
                return false;
            }
        }

        return super.isPlacementChunk(chunkGeneratorStructureState, x, z);
    }

    @Override
    public @NotNull StructurePlacementType<?> type() {
        return PlacementRegistry.ADVANCED_RANDOM_SPREAD.get();
    }

    public record SuperExclusionZone(HolderSet<StructureSet> otherSet, int chunkCount) {
        public static final Codec<SuperExclusionZone> CODEC = RecordCodecBuilder.create((builder) ->
                builder.group(
                        RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC).fieldOf("other_set").forGetter(SuperExclusionZone::otherSet),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("chunk_count").forGetter(SuperExclusionZone::chunkCount))
                    .apply(builder, SuperExclusionZone::new));

        boolean isPlacementForbidden(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int z) {
            for(Holder<StructureSet> holder : this.otherSet()) {
                if (chunkGeneratorStructureState.hasStructureChunkInRange(holder, x, z, this.chunkCount())) {
                    return true;
                }
            }
            return false;
        }

        public HolderSet<StructureSet> otherSet() {
            return this.otherSet;
        }

        public int chunkCount() {
            return this.chunkCount;
        }
    }
}

