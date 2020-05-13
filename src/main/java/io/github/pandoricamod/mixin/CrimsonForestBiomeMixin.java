package io.github.pandoricamod.mixin;

import io.github.pandoricamod.init.PandoricaEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.CrimsonForestBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrimsonForestBiome.class)
public class CrimsonForestBiomeMixin extends Biome {
    protected CrimsonForestBiomeMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("RETURN"), method = "<init>()V")
    private void init(CallbackInfo info) {
        addSpawn(SpawnGroup.MONSTER, new SpawnEntry(PandoricaEntities.LIQUEFIED_SKELETON, 5, 3, 5));
        addSpawn(SpawnGroup.MONSTER, new SpawnEntry(PandoricaEntities.MAGMATOR, 1, 1, 1));
    }
}
