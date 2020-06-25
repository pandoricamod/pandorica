package io.github.pandoricamod.entity.magmator;

import io.github.pandoricamod.block.EruptionBlock;
import io.github.pandoricamod.init.PandoricaSoundEvents;
import io.github.pandoricamod.init.PandoricaTags;
import io.github.pandoricamod.util.PandoricaCommon;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class MagmatorEntity extends HostileEntity {
    public static final String id = "magmator";

    public MagmatorEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
        this.getNavigation().setCanSwim(true);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void initGoals() {
        goalSelector.add(0, new PoundGoal(this, 0.85D, 3.5D, 1.0D, 1.0D, true));
        goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.add(6, new LookAroundGoal(this));
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal(this, PiglinEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal(this, HoglinEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createHostileAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 75.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D);
    }

    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType<MagmatorEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isOf(Blocks.LAVA) && !world.getBlockState(pos.down()).isIn(PandoricaTags.block.UNSAFE_NETHER_SPAWN) && !world.isAir(pos);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @SuppressWarnings("all")
    static class PoundGoal extends Goal {
        protected final MobEntityWithAi mob;
        protected int ticksUntilAttack;
        private final double velocityMultiplier;
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
        private boolean canDamage = false;
        private boolean hasJumped = false;

        public PoundGoal(MobEntityWithAi mob, double velocityMultiplier, double distanceToStart,
                double maxAttackDistance, double speed, boolean pauseWhenMobIdle) {
            this.mob = mob;
            this.velocityMultiplier = velocityMultiplier;
            this.distanceToStart = distanceToStart;
            this.maxAttackDistance = maxAttackDistance;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            return PandoricaCommon.StaticMeleeAttackGoal.canStart(mob, lastUpdateTime, path,
                    getSquaredMaxAttackDistance());
        }

        public boolean shouldContinue() {
            return PandoricaCommon.StaticMeleeAttackGoal.shouldContinue(mob, pauseWhenMobIdle);
        }

        public void start() {
            PandoricaCommon.StaticMeleeAttackGoal.start(mob, path, speed, updateCountdownTicks);
        }

        public void stop() {
            PandoricaCommon.StaticMeleeAttackGoal.stop(mob);
        }

        public void tick() {
            PandoricaCommon.StaticMeleeAttackGoal.tick(mob, updateCountdownTicks, pauseWhenMobIdle, targetX, targetY,
                    targetZ, speed, ticksUntilAttack);
            checkPoundStatus(mob, velocityMultiplier);
        }

        @SuppressWarnings("all")
        public void checkPoundStatus(MobEntity mob, double velocityMultiplier) {
            LivingEntity target = mob.getTarget();
            boolean isInRange = mob.squaredDistanceTo(mob.getTarget()) <= distanceToStart * distanceToStart;

            if (mob.isOnGround()) {
                if (!isInRange) {
                    canDamage = false;
                }

                if (hasJumped) {
                    poundDrop(target);
                    hasJumped = false;
                }
                if (canDamage) {
                    canDamage = false;
                } else if (isInRange) {
                    poundJump(target);
                    hasJumped = true;
                    canDamage = true;
                }
            }
        }

        private void poundJump(LivingEntity target) {
            Vec3d vec3d = mob.getVelocity();
            Vec3d vec3d2 = new Vec3d(target.getX() - mob.getX(), 0.0D, target.getZ() - mob.getZ());
            if (vec3d2.lengthSquared() > 1.0E-7D) {
                vec3d2 = vec3d2.normalize().multiply(0.4D).add(vec3d.multiply(0.2D));
            }

            mob.setVelocity(vec3d2.x, Math.min(mob.squaredDistanceTo(target), velocityMultiplier), vec3d2.z);
        }

        private void poundDrop(LivingEntity target) {
            if (canDamage) {
                if (target instanceof PlayerEntity) {
                    shieldBlockCooldownCheck((PlayerEntity) target,
                            target.isUsingItem() ? target.getActiveItem() : ItemStack.EMPTY);
                }

                mob.swingHand(Hand.MAIN_HAND);
                mob.tryAttack(target);
            }

            EruptionBlock.erupt(mob.world, new BlockPos(mob.getPos()), 4, false, !mob.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING));
        }

        private void shieldBlockCooldownCheck(PlayerEntity player, ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem() == Items.SHIELD) {
                player.getItemCooldownManager().set(Items.SHIELD, 150);
                mob.world.sendEntityStatus(player, (byte)30);
            }
        }

        protected double getSquaredMaxAttackDistance() {
            return PandoricaCommon.StaticMeleeAttackGoal.getSquaredMaxAttackDistance(maxAttackDistance);
        }
    }
}
