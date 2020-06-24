package io.github.pandoricamod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.pandoricamod.block.EruptionBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
    @Shadow private BlockState block;

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void handleFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir) {
        if (block.getBlock() instanceof EruptionBlock) {
            EruptionBlock.landReplacement(world, new BlockPos(getX(), getY(), getZ()), Math.min((int)fallDistance, 7), true);
        }
    }
}
