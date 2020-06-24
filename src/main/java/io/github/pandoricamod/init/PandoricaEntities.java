package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.entity.liquefied_skeleton.*;
import io.github.pandoricamod.entity.magmator.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PandoricaEntities {
    public static final EntityType<LiquefiedSkeletonEntity> LIQUEFIED_SKELETON = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(Pandorica.MOD_ID, LiquefiedSkeletonEntity.id),
            EntityType.Builder.create(LiquefiedSkeletonEntity::new, SpawnGroup.MONSTER).makeFireImmune()
                    .setDimensions(.7F, 2.4F).build(LiquefiedSkeletonEntity.id));
    public static final EntityType<MagmatorEntity> MAGMATOR = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(Pandorica.MOD_ID, MagmatorEntity.id),
            EntityType.Builder.create(MagmatorEntity::new, SpawnGroup.MONSTER).makeFireImmune()
                    .setDimensions(1.85F, 2.3F).build(MagmatorEntity.id));

    public PandoricaEntities() {
        registerDefaultAttributes(LIQUEFIED_SKELETON, LiquefiedSkeletonEntity.createAbstractSkeletonAttributes());
        registerDefaultAttributes(MAGMATOR, MagmatorEntity.createHostileAttributes());
    }

    public static Identifier texture(String path) {
        return Pandorica.texture("entity/" + path);
    }
    public static void registerDefaultAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }
}
