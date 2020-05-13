package io.github.pandoricamod.block;

import io.github.pandoricamod.init.PandoricaTags;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EruptionBlock extends FallingBlock {
    public static final String id = "eruption_block";

    public EruptionBlock(Settings settings) {
        super(settings);
    }

    public static void landReplacement(World world, BlockPos pos, int range, boolean breakBlock) {
        if (breakBlock) {
            world.breakBlock(pos, true);
        }

        BlockPos hitBlockPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());

        range = Math.min(range, 6) / 2;
        Iterable<BlockPos> area = BlockPos.iterate(hitBlockPos.getX() - range, hitBlockPos.getY(), hitBlockPos.getZ() - range, hitBlockPos.getX() + range, hitBlockPos.getY(), hitBlockPos.getZ() + range);

        for (BlockPos posIteration : area) {
            Block blockIteration = world.getBlockState(posIteration).getBlock();
                if (PandoricaTags.blocks.ERUPTION_BLOCK_CONVERTIBLE.contains(blockIteration)) {
                    Block blockAbove = world.getBlockState(posIteration.up()).getBlock();

                    if (blockAbove == Blocks.AIR || blockAbove == Blocks.CAVE_AIR || blockAbove == Blocks.VOID_AIR) {
                        world.breakBlock(posIteration, false);
                        spawnParticles(world, posIteration);

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
    }
    private static void spawnParticles(World world, BlockPos pos) {
        Random random = new Random();
            int rand = random.nextInt(2) * 2 - 1;
            double x = (double)pos.getX() + 0.5D + 0.25D * (double)rand;
            double y = (float)pos.getY() + random.nextFloat();
            double z = (double)pos.getZ() + 0.5D + 0.25D * (double)rand;
            double delta = 0.25;
            ((ServerWorld)world).spawnParticles(ParticleTypes.LAVA, x, y, z, 3, delta, delta, delta, 0);
    }
    private static void convertBlock(World world, BlockPos pos, Block block) {
        world.setBlockState(pos, block.getDefaultState());
    }
}
