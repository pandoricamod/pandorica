package io.github.pandoricamod.entity.liquefied_skeleton;

import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;

public class LiquefiedSkeletonEntity extends WitherSkeletonEntity {
    public static final String id = "liquefied_skeleton";
    private int outOfLavaDamageTick;

    public LiquefiedSkeletonEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);

        this.setPathfindingPenalty(PathNodeType.LAVA, 0);
    }

    protected SoundEvent getAmbientSound() {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource source) {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_HURT;
    }
    protected SoundEvent getDeathSound() {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_DEATH;
    }
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_STEP, 0.15F, 1.0F);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
        this.targetSelector.add(2, new LiquefiedSkeletonEntity.FollowPlayerIfAngryGoal(this));
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("OutOfLavaDamageTick", this.outOfLavaDamageTick);
    }
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.outOfLavaDamageTick = tag.getInt("OutOfLavaDamageTick");
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return !(this.getPathfindingFavor(new BlockPos(this), world) >= 0.0F);
    }

    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {}
    protected void initEquipment(LocalDifficulty difficulty) {}

    protected void mobTick() {
        super.mobTick();

        if (this.isAlive() && !this.isInLava()) {
            outOfLavaDamageTick++;
            if (outOfLavaDamageTick >= 20) {
                this.damage(DamageSource.DROWN, 2);
                outOfLavaDamageTick = 0;
            }
        } else {
            outOfLavaDamageTick = 0;
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = (float) this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue();
        float g = (float) this.getAttributeInstance(EntityAttributes.ATTACK_KNOCKBACK).getValue();
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity) target).getGroup());
            g += (float) EnchantmentHelper.getKnockback(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            target.setOnFireFor(i * 4);
        }

        boolean bl = target.damage(DamageSource.mob(this), f);
        if (bl) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5F, MathHelper.sin(this.yaw * 0.017453292F), -MathHelper.cos(this.yaw * 0.017453292F));
                this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }

            if (target instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) target;
                this.shieldBlockCooldownCheck(playerEntity, this.getMainHandStack(), playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
            }

            this.dealDamage(this, target);
            this.onAttacking(target);
        }

        return bl;
    }
    private void shieldBlockCooldownCheck(PlayerEntity playerEntity, ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem && itemStack2.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentHelper.getEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                playerEntity.getItemCooldownManager().set(Items.SHIELD, 100);
                this.world.sendEntityStatus(playerEntity, (byte) 30);
            }
        }
    }

    static class FollowPlayerIfAngryGoal extends FollowTargetGoal<PlayerEntity> {
        public FollowPlayerIfAngryGoal(LiquefiedSkeletonEntity liquefied_skeleton) {
            super(liquefied_skeleton, PlayerEntity.class, true);
        }

        public boolean canStart() {
            return super.canStart();
        }
    }
//    static class MoveIntoLavaGoal extends Goal {
//        private final MobEntityWithAi mob;
//
//        public MoveIntoLavaGoal(MobEntityWithAi mob) {
//            this.mob = mob;
//        }
//
//        public boolean canStart() {
//            return this.mob.onGround && !this.mob.world.getFluidState(new BlockPos(this.mob)).matches(FluidTags.LAVA);
//        }
//
//        @SuppressWarnings("all")
//        public void start() {
//            BlockPos blockPos = null;
//            Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.mob.getX() - 2.0D), MathHelper.floor(this.mob.getY() - 2.0D), MathHelper.floor(this.mob.getZ() - 2.0D), MathHelper.floor(this.mob.getX() + 2.0D), MathHelper.floor(this.mob.getY()), MathHelper.floor(this.mob.getZ() + 2.0D));
//            Iterator var3 = iterable.iterator();
//
//            while (var3.hasNext()) {
//                BlockPos blockPos2 = (BlockPos) var3.next();
//                if (this.mob.world.getFluidState(blockPos2).matches(FluidTags.LAVA)) {
//                    blockPos = blockPos2;
//                    break;
//                }
//            }
//
//            if (blockPos != null) {
//                this.mob.getMoveControl().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
//            }
//        }
//    }
}
