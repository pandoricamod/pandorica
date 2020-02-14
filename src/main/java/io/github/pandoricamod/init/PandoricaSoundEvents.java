package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PandoricaSoundEvents {
    public static final SoundEvent ENTITY_MAGMATOR_AMBIENT = register("entity.magmator.ambient");
    public static final SoundEvent ENTITY_MAGMATOR_DEATH = register("entity.magmator.death");
    public static final SoundEvent ENTITY_MAGMATOR_HURT = register("entity.magmator.hurt");
    public static final SoundEvent ENTITY_MAGMATOR_STEP = register("entity.magmator.step");
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_AMBIENT = register("entity.liquefied_skeleton.ambient");
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_DEATH = register("entity.liquefied_skeleton.death");
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_HURT = register("entity.liquefied_skeleton.hurt");
    public static final SoundEvent ENTITY_LIQUEFIED_SKELETON_STEP = register("entity.liquefied_skeleton.step");

    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(Pandorica.MOD_PREFIX + id)));
    }
}
