package net.silvertide.realmsofreverie.utils;

import com.alrex.parcool.api.unstable.Limitation;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.Actions;
import com.alrex.parcool.common.action.impl.*;
import com.alrex.parcool.common.stamina.StaminaType;
import harmonised.pmmo.api.APIUtils;
import net.minecraft.server.level.ServerPlayer;
import net.silvertide.realmsofreverie.config.ServerConfigs;

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
        refreshLimitations(
                player,
                APIUtils.getLevel(ServerConfigs.PARCOOL_LAND_SKILL.get(), player),
                APIUtils.getLevel(ServerConfigs.PARCOOL_WATER_SKILL.get(), player)
        );
    }

    public static void refreshLimitations(ServerPlayer player, long landSkillLevel, long waterSkillLevel) {
        Limitation limitation = Limitation.getIndividual(player);

        if (!limitation.isEnabled()) {
            limitation.enable();
        }

        limitation.setStaminaType(StaminaType.PARAGLIDER);

        Actions.LIST.forEach(action -> limitation.permit(action, false));

        enableActions(LAND_ACTIONS, limitation, landSkillLevel);
        enableActions(WATER_ACTIONS, limitation, waterSkillLevel);

        limitation.apply();
        limitation.save();
    }

    private static void enableActions(Map<Long, List<Class<? extends Action>>> actionMap, Limitation limitation, long skillLevel) {
        actionMap.forEach((level, actions) -> {
            if (level <= skillLevel) {
                actions.forEach(actionClass -> limitation.permit(actionClass, true));
            }
        });
    }

}
