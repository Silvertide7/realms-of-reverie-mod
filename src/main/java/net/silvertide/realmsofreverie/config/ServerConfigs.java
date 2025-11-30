package net.silvertide.realmsofreverie.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<String> PARCOOL_SKILL;

    public static final ModConfigSpec.ConfigValue<Double> XP_MULTI;


    static {
        BUILDER.push("Realms of Reverie - Parcool Settings");

        BUILDER.comment("Project MMO Skill to use for unlocking Parcool Actions.");
        PARCOOL_SKILL = BUILDER.worldRestart().define("parcoolSkill", "acrobatics");


        BUILDER.push("XP Multiplier for all awarded xp granted from a parcool action.");
        XP_MULTI = BUILDER.worldRestart().define("vaultXpMultiplier", 1.0);
        BUILDER.pop();


        SPEC = BUILDER.build();
    }
}
