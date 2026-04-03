package net.silvertide.realmsofreverie.commands;


//import com.alrex.parcool.api.unstable.Limitation;
//import com.alrex.parcool.common.action.Action;
//import com.alrex.parcool.common.action.Actions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import harmonised.pmmo.api.APIUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.silvertide.realmsofreverie.config.ServerConfigs;
import net.silvertide.realmsofreverie.utils.ParcoolUtils;

public class CmdNodeAdmin {
    private static final String TARGET_ARG = "Target";

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("admin")
                .requires(p -> p.hasPermission(2))
                .then(Commands.argument(TARGET_ARG, EntityArgument.players())
                        .then(Commands.literal("parcool")
                            .then(Commands.literal("refresh")
                                    .executes(CmdNodeAdmin::enableParcoolLimitations)
                            )
                            .then(Commands.literal("debug")
                                    .executes(CmdNodeAdmin::debugParcoolLimitations)
                            )
                        )
                );
    }

    public static int enableParcoolLimitations(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, TARGET_ARG)) {
//            ParcoolUtils.refreshLimitations(player);
        }
        return 1;
    }

    public static int debugParcoolLimitations(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, TARGET_ARG)) {
            String landSkill = ServerConfigs.PARCOOL_LAND_SKILL.get();
            String waterSkill = ServerConfigs.PARCOOL_WATER_SKILL.get();
            long landLevel = APIUtils.getLevel(landSkill, player);
            long waterLevel = APIUtils.getLevel(waterSkill, player);

            source.sendSuccess(() -> Component.literal("=== ParCool Debug for " + player.getName().getString() + " ==="), false);
            source.sendSuccess(() -> Component.literal("PMMO " + landSkill + " level: " + landLevel), false);
            source.sendSuccess(() -> Component.literal("PMMO " + waterSkill + " level: " + waterLevel), false);

//            Limitation limitation = Limitation.getIndividual(player);
//            source.sendSuccess(() -> Component.literal("Individual limitation enabled: " + limitation.isEnabled()), false);
//            source.sendSuccess(() -> Component.literal("Stamina type: " + limitation.getStaminaType()), false);
//
//            StringBuilder permitted = new StringBuilder("Permitted: ");
//            StringBuilder denied = new StringBuilder("Denied: ");
//            for (Class<? extends Action> action : Actions.LIST) {
//                String name = action.getSimpleName();
//                if (limitation.isPermitted(action)) {
//                    permitted.append(name).append(", ");
//                } else {
//                    denied.append(name).append(", ");
//                }
//            }
//            String permStr = permitted.toString();
//            String denStr = denied.toString();
//            source.sendSuccess(() -> Component.literal(permStr), false);
//            source.sendSuccess(() -> Component.literal(denStr), false);
        }
        return 1;
    }
}
