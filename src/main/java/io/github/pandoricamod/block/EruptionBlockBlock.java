package io.github.pandoricamod.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EruptionBlockBlock extends FallingBlock {
    public EruptionBlockBlock(Settings settings) {
        super(settings);
    }

    public void onLanding(World world, BlockPos blockPos, BlockState blockState_1, BlockState blockState_2) {
        world.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, blockState_1), 1, 1,
                1, 1.0D, 1.0D, 1.0D);

        super.onLanding(world, blockPos, blockState_1, blockState_2);
    }
}
