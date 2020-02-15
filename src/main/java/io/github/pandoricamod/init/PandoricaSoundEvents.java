package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.entity.liquefied_skeleton.LiquefiedSkeletonEntity;
import io.github.pandoricamod.entity.magmator.MagmatorEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PandoricaSoundEvents {
    public static final String magmator = MagmatorEntity.id;
    public static final String liquefied_skeleton = LiquefiedSkeletonEntity.id;

    public static final SoundEvent ENTITY_MAGMATOR_AMBIENT = entityAmbient(magmator);
    public static final SoundEvent ENTITY_MAGMATOR_DEATH = entityDeath(magmator);
    public static final SoundEvent ENTITY_MAGMATOR_HURT = entityHurt(magmator);
    public static final SoundEvent ENTITY_MAGMATOR_STEP = entityStep(magmator);
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_AMBIENT = entityAmbient(liquefied_skeleton);
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_DEATH = entityDeath(liquefied_skeleton);
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_HURT = entityHurt(liquefied_skeleton);

    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(Pandorica.MOD_ID, id)));
    }
    private static SoundEvent entityAmbient(String entity) {
        return register("entity." + entity + ".ambient");
    }
    private static SoundEvent entityDeath(String entity) {
        return register("entity." + entity + ".death");
    }
    private static SoundEvent entityHurt(String entity) {
        return register("entity." + entity + ".hurt");
    }
    private static SoundEvent entityStep(String entity) {
        return register("entity." + entity + ".step");
    }
}
