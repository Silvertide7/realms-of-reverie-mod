package net.silvertide.realmsofreverie.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> DEBUG;
    public static final ModConfigSpec.ConfigValue<String> PARCOOL_LAND_SKILL;
    public static final ModConfigSpec.ConfigValue<String> PARCOOL_WATER_SKILL;

    public static final ModConfigSpec.ConfigValue<Double> XP_MULTI;


    static {
        BUILDER.push("Realms of Reverie - Parcool Settings");
        DEBUG = BUILDER.define("debug", false);

        BUILDER.comment("Project MMO Skill to use for unlocking Parcool Actions on land.");
        PARCOOL_LAND_SKILL = BUILDER.define("parcoolLandSkill", "acrobatics");

        BUILDER.comment("Project MMO Skill to use for unlocking Parcool Actions in water.");
        PARCOOL_WATER_SKILL = BUILDER.define("parcoolWaterSkill", "swimming");

        BUILDER.comment("XP Multiplier for all awarded xp granted from a parcool action.");
        XP_MULTI = BUILDER.define("vaultXpMultiplier", 1.0);
        BUILDER.pop();


        SPEC = BUILDER.build();
    }
}
