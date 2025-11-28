package net.silvertide.realmsofreverie.commands;


import com.alrex.parcool.api.unstable.Limitation;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.silvertide.realmsofreverie.RealmsOfReverie;
import net.silvertide.realmsofreverie.utils.ParcoolUtils;

public class CmdNodeAdmin {
    private static final String TARGET_ARG = "Target";

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("admin")
                .requires(p -> p.hasPermission(2))
                .then(Commands.argument(TARGET_ARG, EntityArgument.players())
                        .then(Commands.literal("parcool")
                            .then(Commands.literal("disable")
                                .executes(CmdNodeAdmin::disableParcoolLimitations)
                            )
                            .then(Commands.literal("update")
                                    .executes(CmdNodeAdmin::enableParcoolLimitations)
                            )
                        )
                );
    }

    public static int disableParcoolLimitations(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, TARGET_ARG)) {
            Limitation limitation = Limitation.getIndividual(player);
            ParcoolUtils.disableAllLimitations(limitation);
            RealmsOfReverie.LOGGER.info("Disabling " + player.getDisplayName() + "'s parcool actions");
        }
        return 0;
    }

    public static int enableParcoolLimitations(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, TARGET_ARG)) {
            Limitation limitation = Limitation.getIndividual(player);
            ParcoolUtils.enableParcoolLimitations(player, limitation);
        }
        return 0;
    }
}
