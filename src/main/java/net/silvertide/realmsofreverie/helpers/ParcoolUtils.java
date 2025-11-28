package net.silvertide.realmsofreverie.helpers;

import com.alrex.parcool.api.unstable.Limitation;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.Actions;
import com.alrex.parcool.common.action.impl.Crawl;
import harmonised.pmmo.api.APIUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;

public final class ParcoolUtils {

    public static final Map<Long, List<Class<? extends Action>>> SKILL_LIMITATIONS =
            Map.of(
                    1L, List.of(Crawl.class)
            );

    private ParcoolUtils() { throw new AssertionError("Utility class");}

    public static void disableAllLimitations(Limitation limitation) {
        Actions.LIST.stream().forEach(action -> {
            limitation.permit(action, false);
        });
    }

    public static void updateParcoolLimitations(ServerPlayer serverPlayer, Limitation limitation) {
        long acrobaticsLevel = APIUtils.getLevel("acrobatics", serverPlayer);
        long maxLevel = SKILL_LIMITATIONS.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        long effectiveMax = Math.min(acrobaticsLevel, maxLevel);

        for (long skillLevel = 1L; skillLevel <= effectiveMax; skillLevel++) {
            enableAcrobaticsLevelActions(skillLevel, limitation);
        }
    }

    public static void enableAcrobaticsLevelActions(long skillLevel, Limitation limitation) {
        var actions = SKILL_LIMITATIONS.get(skillLevel);
        if (actions == null || actions.isEmpty()) {
            return;
        }

        actions.forEach(actionClass -> {
            limitation.permit(actionClass, true);
        });
    }
}
