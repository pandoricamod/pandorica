package io.github.pandoricamod.block;

import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EruptionBlock extends FallingBlock {
    public EruptionBlock(Settings settings) {
        super(settings);
    }

    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos) {
        super.onLanding(world, pos, fallingBlockState, currentStateInPos);

        Random random = new Random();
        for(int i = 0; i < 100; ++i) {
            int j = random.nextInt(2) * 2 - 1;
            int k = random.nextInt(2) * 2 - 1;
            double x = (double)pos.getX() + 0.5D + 0.25D * (double)j;
            double y = (float)pos.getY() + random.nextFloat();
            double z = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
            double dx = random.nextFloat() * (float)j;
            double dy = ((double)random.nextFloat() - 0.5D) * 0.125D;
            double dz = random.nextFloat() * (float)k;
            world.addParticle(ParticleTypes.CRIMSON_SPORE, x, y, z, dx, dy, dz);
            world.addParticle(ParticleTypes.LAVA, x, y, z, dx, dy, dz);
        }

        landReplacement(world, pos, 1);
        world.breakBlock(pos, true);
    }

    public static void landReplacement(World world, BlockPos pos, int fallDistance) {
        BlockPos hitBlockPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        Block hitBlock = world.getBlockState(hitBlockPos).getBlock();
        Block[] convertible = {Blocks.STONE, Blocks.NETHERRACK, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.MAGMA_BLOCK};

        for (Block i:convertible) {
            if (hitBlock == i) {
                world.breakBlock(hitBlockPos, false);

                if (i == Blocks.STONE) {
                    convertBlock(world, hitBlockPos, Blocks.NETHERRACK);
                } else if (i == Blocks.NETHERRACK) {
                    convertBlock(world, hitBlockPos, Blocks.MAGMA_BLOCK);
                } else if (i == Blocks.CRIMSON_NYLIUM) {
                    convertBlock(world, hitBlockPos, Blocks.MAGMA_BLOCK);
                } else if (i == Blocks.WARPED_NYLIUM) {
                    convertBlock(world, hitBlockPos, Blocks.MAGMA_BLOCK);
                } else if (i == Blocks.MAGMA_BLOCK) {
                    convertBlock(world, hitBlockPos, Blocks.LAVA);
                }

                break;
            }
        }
    }
    private static void convertBlock(World world, BlockPos pos, Block block) {
        world.setBlockState(pos, block.getDefaultState());
    }
}
