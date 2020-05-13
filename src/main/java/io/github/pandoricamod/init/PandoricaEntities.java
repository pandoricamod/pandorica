package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.entity.liquefied_skeleton.*;
import io.github.pandoricamod.entity.magmator.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public class PandoricaEntities {
    public static final EntityType<LiquefiedSkeletonEntity> LIQUEFIED_SKELETON = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(Pandorica.MOD_ID, LiquefiedSkeletonEntity.id),
            EntityType.Builder.create(LiquefiedSkeletonEntity::new, SpawnGroup.MONSTER).makeFireImmune()
                    .setDimensions(.7F, 2.4F).build(LiquefiedSkeletonEntity.id));
    public static final EntityType<MagmatorEntity> MAGMATOR = Registry.register(Registry.ENTITY_TYPE,
            new Identifier(Pandorica.MOD_ID, MagmatorEntity.id),
            EntityType.Builder.create(MagmatorEntity::new, SpawnGroup.MONSTER).makeFireImmune()
                    .setDimensions(1.85F, 2.3F).build(MagmatorEntity.id));

    public static Identifier texture(String path) {
        return Pandorica.texture("entity/" + path);
    }
}
