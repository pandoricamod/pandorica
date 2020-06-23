package io.github.pandoricamod;

import net.fabricmc.api.ModInitializer;

public class Pandorica implements ModInitializer {
    public static final String MOD_ID = "pandorica";
    public static final String MOD_NAME = "Pandorica";

	@Override
	public void onInitialize() {
		System.out.println("Loaded " + MOD_NAME);
	}
}
