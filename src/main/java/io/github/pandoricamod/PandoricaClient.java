package io.github.pandoricamod;

import net.fabricmc.api.ClientModInitializer;
import io.github.pandoricamod.init.*;

public class PandoricaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new PandoricaSoundEvents();

        PandoricaEntities.registerRenderers();
    }
}
