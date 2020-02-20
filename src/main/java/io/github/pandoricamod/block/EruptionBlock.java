package io.github.pandoricamod.block;

import io.github.pandoricamod.init.PandoricaTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EruptionBlock extends FallingBlock {
    public static final String id = "eruption_block";

    public EruptionBlock(Settings settings) {
        super(settings);
    }

    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos) {
        super.onLanding(world, pos, fallingBlockState, currentStateInPos);

        spawnEffects(world, pos);
        landReplacement(world, pos, 1);
        world.breakBlock(pos, true);
    }

    @Environment(EnvType.CLIENT)
    private static void spawnEffects(World world, BlockPos pos) {
        Random random = new Random();
        for(int i = 0; i < 300; ++i) {
            int j = random.nextInt(2) * 2 - 1;
            int k = random.nextInt(2) * 2 - 1;
            double x = (double)pos.getX() + 0.5D + 0.25D * (double)j;
            double y = (float)pos.getY() + random.nextFloat();
            double z = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
            double dx = random.nextFloat() * (float)j;
            double dy = ((double)random.nextFloat() - 0.5D) * 0.125D;
            double dz = random.nextFloat() * (float)k;
            world.addParticle(ParticleTypes.LAVA, x, y, z, dx, dy, dz);
        }
    }

    public static void landReplacement(World world, BlockPos pos, int range) {
        BlockPos hitBlockPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());

        range = Math.min(range, 6) / 2;
        Iterable<BlockPos> area = BlockPos.iterate(hitBlockPos.getX() - range, hitBlockPos.getY(), hitBlockPos.getZ() - range, hitBlockPos.getX() + range, hitBlockPos.getY(), hitBlockPos.getZ() + range);

        for (BlockPos posIteration : area) {
            Block blockIteration = world.getBlockState(posIteration).getBlock();
                if (PandoricaTags.blocks.ERUPTION_BLOCK_CONVERTIBLE.contains(blockIteration)) {
                    world.breakBlock(posIteration, false);

                    if (blockIteration == Blocks.MAGMA_BLOCK) {
                        convertBlock(world, posIteration, Blocks.LAVA);
                    } else if (blockIteration == Blocks.NETHERRACK) {
                        convertBlock(world, posIteration, Blocks.MAGMA_BLOCK);
                    } else if (blockIteration == Blocks.STONE || blockIteration == Blocks.COBBLESTONE) {
                        convertBlock(world, posIteration, Blocks.NETHERRACK);
                    } else if (blockIteration == Blocks.WARPED_NYLIUM || blockIteration == Blocks.CRIMSON_NYLIUM) {
                        convertBlock(world, posIteration, Blocks.NETHERRACK);
                    }
            }
        }
    }
    private static void convertBlock(World world, BlockPos pos, Block block) {
        world.setBlockState(pos, block.getDefaultState());
    }
}
