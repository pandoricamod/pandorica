package io.github.pandoricamod;

import io.github.pandoricamod.entity.liquefied_skeleton.*;
import io.github.pandoricamod.entity.magmator.*;
import io.github.pandoricamod.init.PandoricaEntities;
import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class PandoricaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new PandoricaSoundEvents();
        registerEntityRenderers();

        System.out.println("Loaded " + Pandorica.MOD_NAME + ".client");
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(
            PandoricaEntities.LIQUEFIED_SKELETON,
            (entityRenderDispatcher, context) -> new LiquefiedSkeletonEntityRenderer(entityRenderDispatcher)
        );
        EntityRendererRegistry.INSTANCE.register(
            PandoricaEntities.MAGMATOR,
            (entityRenderDispatcher, context) -> new MagmatorEntityRenderer(
                entityRenderDispatcher,
                new MagmatorEntityModel()
                , 0.7f
            )
        );
    }
}
