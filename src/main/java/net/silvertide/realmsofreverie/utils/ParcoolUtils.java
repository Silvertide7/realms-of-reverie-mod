package net.silvertide.realmsofreverie.utils;

import com.alrex.parcool.api.unstable.Limitation;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.Actions;
import com.alrex.parcool.common.action.impl.Crawl;
import harmonised.pmmo.api.APIUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;

public final class ParcoolUtils {
    public static final String PARCOOL_SKILL = "acrobatics";

    private static final Map<Long, List<Class<? extends Action>>> SKILL_LIMITATIONS =
            Map.of(
                    1L, List.of(Crawl.class)
            );

    private ParcoolUtils() { throw new AssertionError("Utility class");}

    public static void refreshLimitations(ServerPlayer player) {
        refreshLimitations(player, APIUtils.getLevel(PARCOOL_SKILL, player));
    }

    public static void refreshLimitations(ServerPlayer player, long skillLevel) {
        Limitation limitation = Limitation.getIndividual(player);
        turnOnLimitations(limitation);
        disableAllLimitations(limitation);
        enableParcoolLimitations(limitation, skillLevel);
        limitation.apply();
    }

    private static void turnOnLimitations(Limitation limitation) {
        if(!limitation.isEnabled()) limitation.enable();
    }

    private static void disableAllLimitations(Limitation limitation) {
        Actions.LIST.forEach(action -> {
            limitation.permit(action, false);
        });
    }

    private static void enableParcoolLimitations(Limitation limitation, long skillLevel) {
        long maxLevel = SKILL_LIMITATIONS.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        long effectiveMax = Math.min(skillLevel, maxLevel);

        for (long i = 1L; i <= effectiveMax; i++) {
            enableAcrobaticsLevelActions(i, limitation);
        }
    }

    private static void enableAcrobaticsLevelActions(long skillLevel, Limitation limitation) {
        var actions = SKILL_LIMITATIONS.get(skillLevel);
        if (actions == null || actions.isEmpty()) {
            return;
        }

        actions.forEach(actionClass -> {
            limitation.permit(actionClass, true);
        });
    }
}
