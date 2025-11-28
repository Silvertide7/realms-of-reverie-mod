package net.silvertide.realmsofreverie.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CmdRoot {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ror")
                .then(CmdNodeAdmin.register(dispatcher)));
    }
}
