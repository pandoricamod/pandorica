package io.github.pandoricamod.block.soul_bricks;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class SoulBricksBlock extends Block {
    public static final BooleanProperty LAVALOGGED = BooleanProperty.of("lavalogged");

    public SoulBricksBlock() {
        super(AbstractBlock.Settings.of(Material.STONE, MaterialColor.BROWN).sounds(BlockSoundGroup.STONE).lightLevel((blockStatex) -> 8));
        this.setDefaultState(this.getDefaultState().with(LAVALOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(LAVALOGGED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LAVALOGGED)) SoulBricksBlock.spawnParticles(world, pos, random);
    }

    @Override
    @SuppressWarnings({"deprecated","unused"})
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return setLavalogged(SoulBricksBlock.LAVALOGGED, state, world, pos, player, hand);
    }

    @Override
    public void onBroken(IWorld world, BlockPos pos, BlockState state) {
        if (state.get(LAVALOGGED)) world.setBlockState(pos, Blocks.LAVA.getDefaultState().with(FluidBlock.LEVEL, 8), 11);
    }

    public static ActionResult setLavalogged(BooleanProperty lavalogged, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!state.get(lavalogged) && player.inventory.getMainHandStack().getItem().equals(Items.LAVA_BUCKET)) {
            world.breakBlock(pos, false, player);
            world.setBlockState(pos, state.with(lavalogged, true));
            world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!player.isCreative()) {
                player.setStackInHand(hand, new ItemStack(Items.AIR));
                player.giveItemStack(new ItemStack(Items.BUCKET));
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    public static void spawnParticles(World world, BlockPos pos, Random random) {
        BlockPos blockPos = pos.up();
        if (world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos).isOpaque()) {
            if (random.nextInt(100) == 0) {
                double x = (float)pos.getX() + random.nextFloat();
                double y = pos.getY() + 1;
                double z = (float)pos.getZ() + random.nextFloat();
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
                world.playSound(x, y, z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }
    }
}
