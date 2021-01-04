package com.tol.itemstages;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ConfigurationHandler {
	public static final String CATEGORY_COMPATIBILITY = "compatibility";

	public static ForgeConfigSpec SERVER_CONFIG;

	public static ForgeConfigSpec.BooleanValue JEIRestrictionsEnabled;

	static {
		final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

		SERVER_BUILDER.comment("Compatibility settings").push(CATEGORY_COMPATIBILITY);
		JEIRestrictionsEnabled = SERVER_BUILDER.comment("Enable or disable JEI functionality based on item and recipe restrictions").define("JEIFunctionalityEnabled", true);
		SERVER_BUILDER.pop();

		SERVER_CONFIG = SERVER_BUILDER.build();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
		configData.load();
		spec.setConfig(configData);
	}
}
