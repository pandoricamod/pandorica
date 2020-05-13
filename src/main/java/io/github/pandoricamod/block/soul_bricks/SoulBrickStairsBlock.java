package io.github.pandoricamod.block.soul_bricks;

import com.terraformersmc.terraform.block.TerraformStairsBlock;
import io.github.pandoricamod.init.PandoricaBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class SoulBrickStairsBlock extends TerraformStairsBlock {
    public static final BooleanProperty LAVALOGGED = BooleanProperty.of("lavalogged");

    public SoulBrickStairsBlock() {
        super(PandoricaBlocks.BlockWithDecorInfo.SOUL_BRICKS.base, AbstractBlock.Settings.copy(PandoricaBlocks.BlockWithDecorInfo.SOUL_BRICKS.base).lightLevel(blockStatex -> blockStatex.get(LAVALOGGED) ? 6 : 0));

        this.setDefaultState(this.getDefaultState().with(LAVALOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING, HALF, SHAPE, WATERLOGGED, LAVALOGGED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LAVALOGGED)) SoulBricksBlock.spawnParticles(world, pos, random);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SoulBricksBlock.setLavalogged(LAVALOGGED, state, world, pos, player, hand);
    }

    @Override
    public void onBroken(IWorld world, BlockPos pos, BlockState state) {
        if (state.get(LAVALOGGED)) world.setBlockState(pos, Blocks.LAVA.getDefaultState().with(FluidBlock.LEVEL, 1), 11);
    }
}
