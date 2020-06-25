package io.github.pandoricamod.block;

import java.util.Random;

import io.github.pandoricamod.init.PandoricaBlocks;
import io.github.pandoricamod.init.PandoricaTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class EruptionBlock extends FallingBlock {
    public static final String id = "eruption_block";

    public EruptionBlock() {
        super(AbstractBlock.Settings.copy(Blocks.NETHERRACK));
    }

    public static void erupt(World world, BlockPos pos, int range, boolean breakBlock, boolean onlyVisual) {
        if (breakBlock) world.breakBlock(pos, true);

        BlockPos hitBlockPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());

        range = Math.min(range, 6) / 2;
        Iterable<BlockPos> area = BlockPos.iterate(hitBlockPos.getX() - range, hitBlockPos.getY(), hitBlockPos.getZ() - range, hitBlockPos.getX() + range, hitBlockPos.getY(), hitBlockPos.getZ() + range);

        for (BlockPos posIteration : area) {
            Block block = world.getBlockState(posIteration).getBlock();
            if (PandoricaTags.block.ERUPTION_BLOCK_CONVERTIBLE.contains(block) && PandoricaTags.block.AIR.contains(world.getBlockState(posIteration.up()).getBlock())) {
                Block newBlock = block;
                if (!onlyVisual) {
                    switch (Registry.BLOCK.getId(block).toString()) {
                        case "minecraft:stone":
                            newBlock = Blocks.COBBLESTONE;
                            break;
                        case "minecraft:crimson_nylium":
                        case "minecraft:warped_nylium":
                        case "minecraft:cobblestone":
                            newBlock = Blocks.NETHERRACK;
                            break;
                        case "minecraft:netherrack":
                            newBlock = PandoricaBlocks.MAGMATIC_NETHERRACK;
                            break;
                        case "pandorica:magmatic_netherrack":
                            newBlock = Blocks.MAGMA_BLOCK;
                            break;
                        case "minecraft:magma_block":
                            newBlock = Blocks.LAVA;
                            break;
                    }
                }

                world.breakBlock(posIteration, false);

                Random random = new Random();
                int rand = random.nextInt(2) * 2 - 1;
                double delta = 0.25;
                int count = 8;
                for (int i = 0; i < count; i++) {
                    double x = (double)posIteration.getX() + 0.5D + 0.25D * (double)rand;
                    double y = (float)posIteration.getY() + random.nextFloat();
                    double z = (double)posIteration.getZ() + 0.5D + 0.25D * (double)rand;

                    world.addParticle(ParticleTypes.LAVA, x, y, z, delta, delta, delta);
                }

                world.setBlockState(posIteration, newBlock.getDefaultState());
            }
        }
    }
    public static void erupt(World world, BlockPos pos, int range, boolean breakBlock) {
        erupt(world, pos, range, breakBlock, false);
    }
}
