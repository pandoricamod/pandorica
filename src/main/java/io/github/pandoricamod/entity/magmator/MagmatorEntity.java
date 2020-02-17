package io.github.pandoricamod.entity.magmator;

import io.github.pandoricamod.entity.ai.goal.MagmatorPounceAtTargetGoal;
import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class MagmatorEntity extends HostileEntity {
    public static final String id = "magmator";

    public MagmatorEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    protected SoundEvent getAmbientSound() {
        return PandoricaSoundEvents.ENTITY_MAGMATOR_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource source) {
        return PandoricaSoundEvents.ENTITY_MAGMATOR_HURT;
    }
    protected SoundEvent getDeathSound() {
        return PandoricaSoundEvents.ENTITY_MAGMATOR_DEATH;
    }
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(PandoricaSoundEvents.ENTITY_MAGMATOR_STEP, 0.15F, 1.0F);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void initGoals() {
        goalSelector.add(0, new MagmatorPounceAtTargetGoal(this, 0.85D, 3.5D, 1.0D, 1.0D, true));
        goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.add(6, new LookAroundGoal(this));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return world.getBlockState(new BlockPos(getX(), getY() - 1, getZ())).getBlock() == Blocks.CRIMSON_NYLIUM;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }



    public static void pounceBlockReplacement(LivingEntity mob) {
        spawnPoof(mob);

        BlockPos blockPos = new BlockPos(mob.getX(), mob.getY() - 1, mob.getZ());
        BlockState blockState = mob.world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (!blockState.isAir() && blockState.getMaterial() != Material.FIRE) {
            if (mob.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && block == Blocks.STONE) {
                mob.world.removeBlock(blockPos, false);
            }
        }
    }

    private static void spawnPoof(LivingEntity mob) {
        Random random = mob.getRandom();
        if (mob.world.isClient) {
            for(int j = 0; j < 20; ++j) {
                float f = random.nextFloat() * 6.2831855F;
                float g = random.nextFloat() * 0.5F + 0.5F;
                float h = MathHelper.sin(f) * 1.0F * g;
                float k = MathHelper.cos(f) * 1.0F * g;
                mob.world.addParticle(ParticleTypes.POOF, mob.getX() + (double)h, mob.getY(), mob.getZ() + (double)k, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
