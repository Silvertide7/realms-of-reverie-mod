package net.silvertide.realmsofreverie.utils;

//import com.alrex.parcool.api.unstable.Limitation;
//import com.alrex.parcool.common.action.Action;
//import com.alrex.parcool.common.action.Actions;
//import com.alrex.parcool.common.action.impl.*;
//import com.alrex.parcool.common.stamina.StaminaType;
import harmonised.pmmo.api.APIUtils;
import net.minecraft.server.level.ServerPlayer;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.config.ServerConfigs;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ParcoolUtils {
    private ParcoolUtils() { throw new AssertionError("Utility class");}

    // Tracks the last known level per player per skill so we can skip redundant
    // refreshes from PMMO's isLevelUp() spam. Key = "uuid:skill", Value = level.
    private static final Map<String, Long> LAST_LEVELS = new ConcurrentHashMap<>();

    private static String levelKey(UUID playerUUID, String skill) {
        return playerUUID.toString() + ":" + skill;
    }

    public static long getLastLevel(UUID playerUUID, String skill) {
        return LAST_LEVELS.getOrDefault(levelKey(playerUUID, skill), -1L);
    }

    public static void setLastLevel(UUID playerUUID, String skill, long level) {
        LAST_LEVELS.put(levelKey(playerUUID, skill), level);
    }

//    // Index = skill level. Actions at index i unlock at land skill level i.
//    private static final List<List<Class<? extends Action>>> LAND_ACTIONS = List.of(
//            /* 0  */ List.of(RideZipline.class, SkyDive.class, QuickTurn.class),
//            /* 1  */ List.of(Crawl.class),
//            /* 2  */ List.of(ChargeJump.class),
//            /* 3  */ List.of(FastRun.class, Slide.class),
//            /* 4  */ List.of(Vault.class),
//            /* 5  */ List.of(WallJump.class),
//            /* 6  */ List.of(ClingToCliff.class, ClimbPoles.class, ClimbUp.class, JumpFromBar.class, HangDown.class),
//            /* 7  */ List.of(BreakfallReady.class, Tap.class, Roll.class),
//            /* 8  */ List.of(CatLeap.class, Dodge.class),
//            /* 9  */ List.of(WallSlide.class),
//            /* 10 */ List.of(Flipping.class, HideInBlock.class),
//            /* 11 */ List.of(VerticalWallRun.class),
//            /* 12 */ List.of(HorizontalWallRun.class)
//    );
//
//    // Index = skill level. Actions at index i unlock at water skill level i.
//    // Indices 0-4 and 6 are empty (no water actions at those levels).
//    private static final List<List<Class<? extends Action>>> WATER_ACTIONS = List.of(
//            /* 0 */ List.of(),
//            /* 1 */ List.of(),
//            /* 2 */ List.of(),
//            /* 3 */ List.of(),
//            /* 4 */ List.of(),
//            /* 5 */ List.of(FastSwim.class),
//            /* 6 */ List.of(),
//            /* 7 */ List.of(Dive.class)
//    );
//
//    public static void refreshLimitations(ServerPlayer player) {
//        refreshLimitations(
//                player,
//                APIUtils.getLevel(ServerConfigs.PARCOOL_LAND_SKILL.get(), player),
//                APIUtils.getLevel(ServerConfigs.PARCOOL_WATER_SKILL.get(), player));
//    }
//
//    public static void refreshLimitations(ServerPlayer player, long landSkillLevel, long waterSkillLevel) {
//        Limitation limitation = Limitation.getIndividual(player);
//
//        RealmsOfReverie.LOGGER.info("[RoR ParCool]   limitation enabled before: {}", limitation.isEnabled());
//
//        if (!limitation.isEnabled()) {
//            limitation.enable();
//        }
//
//        limitation.setStaminaType(StaminaType.PARAGLIDER);
//
//        Actions.LIST.forEach(action -> limitation.permit(action, false));
//
//        enableActions(LAND_ACTIONS, limitation, landSkillLevel);
//        enableActions(WATER_ACTIONS, limitation, waterSkillLevel);
//
//        // Log the final state before applying
//        StringJoiner permitted = new StringJoiner(", ");
//        StringJoiner denied = new StringJoiner(", ");
//        for (Class<? extends Action> action : Actions.LIST) {
//            if (limitation.isPermitted(action)) {
//                permitted.add(action.getSimpleName());
//            } else {
//                denied.add(action.getSimpleName());
//            }
//        }
//        RealmsOfReverie.LOGGER.info("[RoR ParCool]   permitted: {}", permitted);
//        RealmsOfReverie.LOGGER.info("[RoR ParCool]   denied: {}", denied);
//
//        limitation.apply();
//        limitation.save();
//        RealmsOfReverie.LOGGER.info("[RoR ParCool]   apply() and save() complete");
//    }
//
//    private static void enableActions(List<List<Class<? extends Action>>> actionList, Limitation limitation, long skillLevel) {
//        int maxIndex = (int) Math.min(skillLevel, actionList.size() - 1);
//        for (int i = 0; i <= maxIndex; i++) {
//            for (Class<? extends Action> actionClass : actionList.get(i)) {
//                limitation.permit(actionClass, true);
//            }
//        }
//    }

}
