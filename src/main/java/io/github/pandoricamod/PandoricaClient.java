package io.github.pandoricamod;

import io.github.pandoricamod.entity.liquefied_skeleton.LiquefiedSkeletonEntityRenderer;
import io.github.pandoricamod.entity.magmator.MagmatorEntityModel;
import io.github.pandoricamod.entity.magmator.MagmatorEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import io.github.pandoricamod.init.*;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class PandoricaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new PandoricaSoundEvents();

        registerEntityRenderers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(PandoricaEntities.LIQUEFIED_SKELETON,
                (entityRenderDispatcher, context) -> new LiquefiedSkeletonEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(PandoricaEntities.MAGMATOR,
                (entityRenderDispatcher, context) -> new MagmatorEntityRenderer(entityRenderDispatcher, new MagmatorEntityModel(), 0.7f));
    }
}
