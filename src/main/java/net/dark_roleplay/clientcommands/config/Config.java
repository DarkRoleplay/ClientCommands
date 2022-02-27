package net.dark_roleplay.clientcommands.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<String> commandStarter;

	static {
		commandStarter = builder.comment("The prefix for client side commands", "Cannot be set to \"/\"").define("Prefix", "!", Config::validateMarker);

		SPEC = builder.build();
	}

	private static boolean validateMarker(Object val) {
		if (val == null ||
				!(val instanceof String prefix) ||
				prefix.isEmpty()) return false;

		return !prefix.startsWith("/");
	}
}