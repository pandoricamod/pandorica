package io.github.pandoricamod.entity.liquefied_skeleton;

import io.github.pandoricamod.init.PandoricaEntities;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.WitherSkeletonEntityRenderer;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

public class LiquefiedSkeletonEntityRenderer extends WitherSkeletonEntityRenderer {
    private static final String entity_id = LiquefiedSkeletonEntity.id;

    public LiquefiedSkeletonEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1);
    }
    
    @Override
	public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity_1) {
        return PandoricaEntities.texture(entity_id);
    }
}
