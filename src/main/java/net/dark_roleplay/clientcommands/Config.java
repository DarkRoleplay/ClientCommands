package net.dark_roleplay.clientcommands;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> markerChar;

    static
    {
        markerChar = builder.comment("The prefix for client side commands", "Cannot be set to \"/\"", "Single character only").define("Prefix", "!", Config::validateMarker);

        SPEC = builder.build();
    }

    private static boolean validateMarker(Object val)
    {
        if(val == null)
            return false;

        String prefix = (String)val;

        if(prefix.length() != 1)
        {
            return false;
        }

        return !prefix.startsWith("/");
    }
}