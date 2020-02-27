package io.github.pandoricamod.entity.liquefied_skeleton;

import io.github.pandoricamod.entity.PandoricaDamageSource;
import io.github.pandoricamod.init.PandoricaSoundEvents;
import io.github.pandoricamod.util.PandoricaCommon;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

import java.util.EnumSet;

public class LiquefiedSkeletonEntity extends WitherSkeletonEntity {
    public static final String id = "liquefied_skeleton";
    private int outOfLavaDamageTick;

    public LiquefiedSkeletonEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);

        setPathfindingPenalty(PathNodeType.LAVA, 0);
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
        playSound(PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_STEP, 0.15F, 1.0F);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void initGoals() {
        goalSelector.add(0, new AmbushGoal(this, 0.6D, 16.0D, 1.0D, 1.0D, true));
        goalSelector.add(3, new FleeEntityGoal(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.add(6, new LookAroundGoal(this));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("OutOfLavaDamageTick", outOfLavaDamageTick);
    }
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        outOfLavaDamageTick = tag.getInt("OutOfLavaDamageTick");
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        double spawnLevel = 28;
        int lavaCount = 0;

        for (int i = 0; i < 5; i++) {
            if (blockIsLava(getY() - i)) {
                lavaCount++;
            }
        }

        return !blockIsLava(spawnLevel - 1) && lavaCount >= 3;
    }
    private boolean blockIsLava(double y) {
        return world.getBlockState(new BlockPos(getX(), y, getZ())).getFluidState().matches(FluidTags.LAVA);
    }

    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {}
    protected void initEquipment(LocalDifficulty difficulty) {}

    protected void mobTick() {
        super.mobTick();

        if (isAlive() && !isInLava()) {
            outOfLavaDamageTick++;
            if (outOfLavaDamageTick >= 20) {
                damage(DamageSource.DROWN, 2);
                outOfLavaDamageTick = 0;
            }
        } else {
            outOfLavaDamageTick = 0;
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = (float) getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue();
        float g = (float) getAttributeInstance(EntityAttributes.ATTACK_KNOCKBACK).getValue();
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(getMainHandStack(), ((LivingEntity) target).getGroup());
            g += (float) EnchantmentHelper.getKnockback(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            target.setOnFireFor(i * 4);
        }

        boolean bl = target.damage(PandoricaDamageSource.ambush(this), f);
        if (bl) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5F, MathHelper.sin(yaw * 0.017453292F), -MathHelper.cos(yaw * 0.017453292F));
                setVelocity(getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }

            if (target instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) target;
                shieldBlockCooldownCheck(playerEntity, getMainHandStack(), playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
            }

            dealDamage(this, target);
            onAttacking(target);
        }

        return bl;
    }
    private void shieldBlockCooldownCheck(PlayerEntity playerEntity, ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem && itemStack2.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentHelper.getEfficiency(this) * 0.05F;
            if (random.nextFloat() < f) {
                playerEntity.getItemCooldownManager().set(Items.SHIELD, 100);
                world.sendEntityStatus(playerEntity, (byte) 30);
            }
        }
    }

    @SuppressWarnings("all")
    static class AmbushGoal extends Goal {
        protected final MobEntityWithAi mob;
        protected int ticksUntilAttack;
        private final double velocityModifier;
        private final double distanceToStart;
        private final double maxAttackDistance;
        private final double speed;
        private final boolean pauseWhenMobIdle;
        private Path path;
        private int updateCountdownTicks;
        private double targetX;
        private double targetY;
        private double targetZ;
        private long lastUpdateTime;

        public AmbushGoal(MobEntityWithAi mob, double velocityModifier, double distanceToStart, double maxAttackDistance, double speed, boolean pauseWhenMobIdle) {
            this.mob = mob;
            this.velocityModifier = velocityModifier;
            this.distanceToStart = distanceToStart;
            this.maxAttackDistance = maxAttackDistance;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            if (mob.isInLava()) {
                return PandoricaCommon.StaticMeleeAttackGoal.canStart(mob, lastUpdateTime, path, getSquaredMaxAttackDistance());
            } else {
                return false;
            }
        }

        public boolean shouldContinue() {
            if (!mob.isInLava()) {
                return false;
            } else if (!mob.method_24828()) {
                return false;
            } else {
                return PandoricaCommon.StaticMeleeAttackGoal.shouldContinue(mob, pauseWhenMobIdle);
            }
        }

        public void start() {
            PandoricaCommon.StaticMeleeAttackGoal.start(mob, path, speed, updateCountdownTicks);
        }

        public void stop() {
            PandoricaCommon.StaticMeleeAttackGoal.stop(mob);
        }

        public void tick() {
            PandoricaCommon.StaticMeleeAttackGoal.tick(mob, updateCountdownTicks, pauseWhenMobIdle, targetX, targetY, targetZ, speed, ticksUntilAttack);
            checkAmbushStatus(mob);
        }

        public void checkAmbushStatus(MobEntity mob) {
            LivingEntity target = mob.getTarget();

            if (mob.method_24828() && mob.squaredDistanceTo(target) <= distanceToStart * distanceToStart && mob.getY() < target.getY()) {
                ambush(target);
            }
            if (mob.squaredDistanceTo(mob.getTarget()) <= getSquaredMaxAttackDistance()) {
                mob.swingHand(Hand.MAIN_HAND);
                mob.tryAttack(target);
            }
        }

        public void ambush(LivingEntity target) {
            Vec3d vec3d = mob.getVelocity();
            Vec3d vec3d2 = new Vec3d(target.getX() - mob.getX(), 0.0D, target.getZ() - mob.getZ());
            if (vec3d2.lengthSquared() > 1.0E-7D) {
                vec3d2 = vec3d2.normalize().multiply(0.4D).add(vec3d.multiply(0.2D));
            }

            Vec3d rotation = mob.getRotationVector();
            mob.setVelocity(rotation.x * velocityModifier * 2 , Math.min(Math.sqrt(mob.squaredDistanceTo(target)), 3.9D) * velocityModifier, rotation.z * velocityModifier * 2);
        }

        protected double getSquaredMaxAttackDistance() {
            return PandoricaCommon.StaticMeleeAttackGoal.getSquaredMaxAttackDistance(maxAttackDistance);
        }
    }
}
