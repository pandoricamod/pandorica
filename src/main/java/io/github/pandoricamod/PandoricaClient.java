package io.github.pandoricamod;

import com.terraformersmc.terraform.block.TerraformSignBlock;
import com.terraformersmc.terraform.registry.SpriteIdentifierRegistry;
import io.github.pandoricamod.entity.liquefied_skeleton.*;
import io.github.pandoricamod.entity.magmator.*;
import net.fabricmc.api.ClientModInitializer;
import io.github.pandoricamod.init.*;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class PandoricaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new PandoricaSoundEvents();

        registerEntityRenderers();
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(PandoricaEntities.LIQUEFIED_SKELETON,
                (entityRenderDispatcher, context) -> new LiquefiedSkeletonEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(PandoricaEntities.MAGMATOR,
                (entityRenderDispatcher, context) -> new MagmatorEntityRenderer(entityRenderDispatcher, new MagmatorEntityModel(), 0.7f));

        addSigns(
                PandoricaBlocks.WoodBlocksInfo.WEEPING.sign
        );
    }

    private void addSigns(TerraformSignBlock... signs) {
        for(TerraformSignBlock sign: signs) {
            addSign(sign);
        }
    }
    private void addSign(TerraformSignBlock sign) {
        Identifier texture = sign.getTexture();
        SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, texture));
    }
}
