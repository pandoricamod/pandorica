package io.github.pandoricamod;

import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.fabricmc.api.ClientModInitializer;

public class PandoricaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new PandoricaSoundEvents();

        System.out.println("Loaded " + Pandorica.MOD_NAME + ".client");
    }
}
