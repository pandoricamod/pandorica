package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.entity.liquefied_skeleton.*;
import io.github.pandoricamod.entity.magmator.*;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PandoricaEntities {
    public static final EntityType<LiquefiedSkeletonEntity> LIQUEFIED_SKELETON = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(Pandorica.MOD_ID, LiquefiedSkeletonEntity.id),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, LiquefiedSkeletonEntity::new).setImmuneToFire()
                    .size(EntityDimensions.fixed(.7F, 2.4F)).build());
    public static final EntityType<MagmatorEntity> MAGMATOR = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(Pandorica.MOD_ID, MagmatorEntity.id),
            FabricEntityTypeBuilder.create(EntityCategory.MONSTER, MagmatorEntity::new).setImmuneToFire()
                    .size(EntityDimensions.fixed(1.85F, 2.3F)).build());

    public static void registerRenderers() {
        EntityRendererRegistry.INSTANCE.register(LIQUEFIED_SKELETON,
                (entityRenderDispatcher, context) -> new LiquefiedSkeletonEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MAGMATOR,
                (entityRenderDispatcher, context) -> new MagmatorEntityRenderer(entityRenderDispatcher, new MagmatorEntityModel(), 0.7f));
    }
}
