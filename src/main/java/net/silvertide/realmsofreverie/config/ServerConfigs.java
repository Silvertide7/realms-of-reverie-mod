package net.silvertide.realmsofreverie.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<String> PARCOOL_LAND_SKILL;
    public static final ModConfigSpec.ConfigValue<String> PARCOOL_WATER_SKILL;

    static {
        BUILDER.push("Realms of Reverie - Parcool Settings");

        BUILDER.comment("Project MMO Skill to use for unlocking Parcool Actions on land.");
        PARCOOL_LAND_SKILL = BUILDER.define("parcoolLandSkill", "acrobatics");

        BUILDER.comment("Project MMO Skill to use for unlocking Parcool Actions in water.");
        PARCOOL_WATER_SKILL = BUILDER.define("parcoolWaterSkill", "swimming");
        BUILDER.pop();


        SPEC = BUILDER.build();
    }
}
