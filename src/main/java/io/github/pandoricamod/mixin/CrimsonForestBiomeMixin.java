package io.github.pandoricamod.mixin;

import io.github.pandoricamod.init.PandoricaEntities;
import net.minecraft.entity.EntityCategory;
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
        this.addSpawn(EntityCategory.MONSTER, new SpawnEntry(PandoricaEntities.MAGMATOR, 5, 3, 4));
    }
}
