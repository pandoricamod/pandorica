package io.github.pandoricamod.mixin;

import io.github.pandoricamod.init.PandoricaEntities;
import net.minecraft.entity.EntityCategory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.NetherWastesBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherWastesBiome.class)
public class NetherWastesBiomeMixin extends Biome {
    protected NetherWastesBiomeMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("RETURN"), method = "<init>()V")
    private void init(CallbackInfo info) {
        addSpawn(EntityCategory.MONSTER, new SpawnEntry(PandoricaEntities.LIQUEFIED_SKELETON, 5, 3, 5));
    }
}
