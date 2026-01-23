package net.silvertide.realmsofreverie.utils;

import com.alrex.parcool.api.unstable.Limitation;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.Actions;
import com.alrex.parcool.common.action.impl.*;
import com.alrex.parcool.common.stamina.StaminaType;
import com.alrex.parcool.config.ParCoolConfig;
import harmonised.pmmo.api.APIUtils;
import net.minecraft.server.level.ServerPlayer;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.config.ServerConfigs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ParcoolUtils {
    private ParcoolUtils() { throw new AssertionError("Utility class");}

    private static final Map<Long, List<Class<? extends Action>>> LAND_ACTIONS = Map.ofEntries(
            Map.entry(0L, List.of(RideZipline.class, SkyDive.class, QuickTurn.class)),
            Map.entry(1L, List.of(Crawl.class)),
            Map.entry(2L, List.of(ChargeJump.class)),
            Map.entry(3L, List.of(FastRun.class, Slide.class)),
            Map.entry(4L, List.of(Vault.class)),
            Map.entry(5L, List.of(WallJump.class)),
            Map.entry(6L, List.of(ClingToCliff.class, ClimbPoles.class, ClimbUp.class, JumpFromBar.class, HangDown.class)),
            Map.entry(7L, List.of(BreakfallReady.class, Tap.class, Roll.class)),
            Map.entry(8L, List.of(CatLeap.class, Dodge.class)),
            Map.entry(9L, List.of(WallSlide.class)),
            Map.entry(10L, List.of(Flipping.class, HideInBlock.class)),
            Map.entry(11L, List.of(VerticalWallRun.class)),
            Map.entry(12L, List.of(HorizontalWallRun.class))
    );

    private static final Map<Long, List<Class<? extends Action>>> WATER_ACTIONS = Map.ofEntries(
            Map.entry(5L, List.of(FastSwim.class)),
            Map.entry(7L, List.of(Dive.class))
    );

    public static void refreshLimitations(ServerPlayer player) {
        refreshLimitations(player, APIUtils.getLevel(ServerConfigs.PARCOOL_LAND_SKILL.get(), player), APIUtils.getLevel(ServerConfigs.PARCOOL_WATER_SKILL.get(), player));
    }

    public static void refreshLimitations(ServerPlayer player, long landSkillLevel, long waterSkillLevel) {
        RealmsOfReverie.printDebug("Method - refreshLimitations");
        RealmsOfReverie.printDebug("landSkillLevel: " + landSkillLevel);
        RealmsOfReverie.printDebug("waterSkillLevel: " + waterSkillLevel);

        Limitation limitation = Limitation.getIndividual(player);
        printLimitationInformation(limitation);

        turnOnLimitations(limitation);
        setStaminaType(limitation);

        disableAllActions(limitation);
        enableActions(LAND_ACTIONS, limitation, landSkillLevel);
        enableActions(WATER_ACTIONS, limitation, waterSkillLevel);

        printLimitationInformation(limitation);

        limitation.apply();
    }

    private static void turnOnLimitations(Limitation limitation) {
        RealmsOfReverie.printDebug("Method - turnOnLimitations");
        if(!limitation.isEnabled()){
            RealmsOfReverie.printDebug("Limitation was not enabled. Enabling");
            limitation.enable();
        }
    }

    private static void setStaminaType(Limitation limitation) {
        RealmsOfReverie.printDebug("Method - setStaminaType");
        limitation.setStaminaType(StaminaType.PARAGLIDER);
    }

    private static void disableAllActions(Limitation limitation) {
        RealmsOfReverie.printDebug("Method - disableAllActions");
        Actions.LIST.forEach(action -> {
            RealmsOfReverie.printDebug("Disabling Action - " + action.descriptorString());
            limitation.permit(action, false);
        });
    }

    private static void enableActions(Map<Long, List<Class<? extends Action>>> actionMap, Limitation limitation, long skillLevel) {
        RealmsOfReverie.printDebug("Method - enableActions");
        long maxLevel = actionMap.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        long effectiveMax = Math.min(skillLevel, maxLevel);
        RealmsOfReverie.printDebug("Effective Max: " + effectiveMax);

        for (long i = 0L; i <= effectiveMax; i++) {
            enableActionsForLevel(actionMap, i, limitation);
        }
    }

    private static void enableActionsForLevel(Map<Long, List<Class<? extends Action>>> actionMap, long skillLevel, Limitation limitation) {
        RealmsOfReverie.printDebug("Method - enableActionsForLevel");
        var actions = actionMap.get(skillLevel);
        if (actions == null || actions.isEmpty()) {
            return;
        }

        actions.forEach(actionClass -> {
            limitation.permit(actionClass, true);
            RealmsOfReverie.printDebug("Permitting Action: " + actionClass.descriptorString());
        });
    }

    // --- Award XP For Parcool Actions ---

    private static Map<Class<? extends Action>, Integer> ACTION_AWARDS = null;
    private static final List<Class<? extends Action>> ACTIONS_TO_AWARD = List.of(
            Vault.class,
            WallJump.class,
            CatLeap.class
    );

    private static Map<Class<? extends Action>, Integer> getActionAwards() {
        if (ACTION_AWARDS == null) {
            ACTION_AWARDS = new HashMap<>();

            for (Class<? extends Action> actionClass : ACTIONS_TO_AWARD) {
                int stamina = ParCoolConfig.Server.getInstance()
                        .getLeastStaminaConsumptionOf(actionClass);
                ACTION_AWARDS.put(actionClass, stamina);
            }
        }
        return ACTION_AWARDS;
    }

    public static void awardXp(ServerPlayer player, Action action) {
        Integer baseXp = getActionAwards().get(action.getClass());
        if (baseXp == null || baseXp <= 0) return;

        double multiplier = ServerConfigs.XP_MULTI.get();
        long awardedXp = Math.round(baseXp * multiplier);

        if (awardedXp > 0) {
            APIUtils.addXp(ServerConfigs.PARCOOL_LAND_SKILL.get(), player, awardedXp);
        }
    }

    public static void clearActionAwardsCache() {
        ACTION_AWARDS = null;
    }

    public static void printLimitationInformation(Limitation limitation) {
        RealmsOfReverie.printDebug("limitation stamina type: " + limitation.getStaminaType().name());
        RealmsOfReverie.printDebug("Are actions permitted: ");
        Actions.LIST.forEach(action -> {
            RealmsOfReverie.printDebug("Action: " + action.descriptorString() + " isPermitted: " + limitation.isPermitted(action));
        });
    }
}
