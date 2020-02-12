package io.github.pandoricamod.entity.magmator;

import io.github.pandoricamod.Pandorica;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class MagmatorEntityRenderer extends MobEntityRenderer<MagmatorEntity, MagmatorEntityModel> {
    public MagmatorEntityRenderer(EntityRenderDispatcher renderManager, MagmatorEntityModel model, float f) {
        super(renderManager, model, f);
    }

    @Override
    public Identifier getTexture(MagmatorEntity entity) {
        return new Identifier(Pandorica.MOD_PREFIX + "textures/entity/"+ "magmator.png");
    }
}
