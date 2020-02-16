package io.github.pandoricamod.entity.magmator;

import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.EnumSet;

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

    @SuppressWarnings({"rawtypes","unchecked"})
    protected void initGoals() {
        targetSelector.add(1, new FollowTargetGoal(this, PlayerEntity.class, true));
        targetSelector.add(1, new FollowTargetGoal(this, HoglinEntity.class, true));
        goalSelector.add(2, new PounceAtTargetGoal(this, 0.25F, 3.0D));
        goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, 1.0D, true));
        targetSelector.add(4, new RevengeGoal(this));
        goalSelector.add(5, new GoToEntityTargetGoal(this, 0.9D, 32.0F));
        goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));
        goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        goalSelector.add(8, new LookAroundGoal(this));
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return getPathfindingFavor(new BlockPos(this), world) >= 0.0F;
    }

//    @Override
//    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
//        if (!isTouchingWater()) {
//            checkWaterState();
//        }
//
//        if (!world.isClient && fallDistance > 5.0F && onGround) {
//            float f = (float) MathHelper.ceil(fallDistance - 5.0F);
//            if (!landedState.isAir()) {
//                double d = Math.min(0.2F + f / 15.0F, 2.5D);
//                int i = (int)(150.0D * d);
//                ((ServerWorld)world).spawnParticles(ParticleTypes.POOF, getX(), getY(), getZ(), i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
//            }
//        }
//    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    static class PounceAtTargetGoal extends Goal {
        private final MobEntity mob;
        private LivingEntity target;
        private final float velocity;
        private final double distanceToStart;

        public PounceAtTargetGoal(MobEntity rmob, float velocity, double distanceToStart) {
            this.mob = rmob;
            this.velocity = velocity;
            this.distanceToStart = distanceToStart;
            setControls(EnumSet.of(Control.JUMP, Control.MOVE));
        }

        public boolean canStart() {
            if (mob.hasPassengers()) {
                return false;
            } else {
                target = mob.getTarget();
                if (target == null) {
                    return false;
                } else {
                    double d = mob.squaredDistanceTo(target);
                    if (d <= distanceToStart * distanceToStart) {
                        if (!mob.onGround) {
                            return false;
                        } else {
                            return mob.getRandom().nextInt(5) == 0;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        public boolean shouldContinue() {
            return !mob.onGround;
        }

        public void start() {
            Vec3d vec3d = mob.getVelocity();
            Vec3d vec3d2 = new Vec3d(target.getX() - mob.getX(), 0.0D, target.getZ() - mob.getZ());
            if (vec3d2.lengthSquared() > 1.0E-7D) {
                vec3d2 = vec3d2.normalize().multiply(0.4D).add(vec3d.multiply(0.2D));
            }

            mob.setVelocity(vec3d2.x, mob.squaredDistanceTo(target) * velocity, vec3d2.z);

            mob.getEntityWorld().addParticle(ParticleTypes.POOF, mob.getX(), mob.getY(), mob.getZ(), 0.3D, 0.2D, 0.3D);
        }
    }
    static class MeleeAttackGoal extends Goal {
        protected final MobEntityWithAi mob;
        protected int ticksUntilAttack;
        private final double speed;
        private final boolean pauseWhenMobIdle;
        private Path path;
        private int updateCountdownTicks;
        private double targetX;
        private double targetY;
        private double targetZ;
        protected final int attackIntervalTicks = 20;
        private long lastUpdateTime;
        private final double maxAttackDistance;

        public MeleeAttackGoal(MobEntityWithAi mob, double speed, double maxAttackDistance, boolean pauseWhenMobIdle) {
            this.mob = mob;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.maxAttackDistance = maxAttackDistance;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            long l = mob.world.getTime();
            if (l - lastUpdateTime < 20L) {
                return false;
            } else {
                lastUpdateTime = l;
                LivingEntity livingEntity = mob.getTarget();
                if (livingEntity == null) {
                    return false;
                } else if (!livingEntity.isAlive()) {
                    return false;
                } else {
                    path = mob.getNavigation().findPathTo(livingEntity, 0);
                    if (path != null) {
                        return true;
                    } else {
                        return getSquaredMaxAttackDistance(livingEntity) >= mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    }
                }
            }
        }

        public boolean shouldContinue() {
            LivingEntity livingEntity = mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else if (!pauseWhenMobIdle) {
                return !mob.getNavigation().isIdle();
            } else if (!mob.isInWalkTargetRange(new BlockPos(livingEntity))) {
                return false;
            } else {
                return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
            }
        }

        public void start() {
            mob.getNavigation().startMovingAlong(path, speed);
            mob.setAttacking(true);
            updateCountdownTicks = 0;
        }

        public void stop() {
            LivingEntity livingEntity = mob.getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                mob.setTarget(null);
            }

            mob.setAttacking(false);
            mob.getNavigation().stop();
        }

        public void tick() {
            LivingEntity livingEntity = mob.getTarget();
            mob.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
            double d = mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            --updateCountdownTicks;
            if ((pauseWhenMobIdle || mob.getVisibilityCache().canSee(livingEntity)) && updateCountdownTicks <= 0 && (targetX == 0.0D && targetY == 0.0D && targetZ == 0.0D || livingEntity.squaredDistanceTo(targetX, targetY, targetZ) >= 1.0D || mob.getRandom().nextFloat() < 0.05F)) {
                targetX = livingEntity.getX();
                targetY = livingEntity.getY();
                targetZ = livingEntity.getZ();
                updateCountdownTicks = 4 + mob.getRandom().nextInt(7);
                if (d > 1024.0D) {
                    updateCountdownTicks += 10;
                } else if (d > 256.0D) {
                    updateCountdownTicks += 5;
                }

                if (!mob.getNavigation().startMovingTo(livingEntity, speed)) {
                    updateCountdownTicks += 15;
                }
            }

            ticksUntilAttack = Math.max(ticksUntilAttack - 1, 0);
            attack(livingEntity, d);
        }

        protected void attack(LivingEntity target, double squaredDistance) {
            double d = getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && ticksUntilAttack <= 0) {
                ticksUntilAttack = 20;
                mob.swingHand(Hand.MAIN_HAND);
                mob.tryAttack(target);
            }

        }

        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return maxAttackDistance * maxAttackDistance;
        }
    }
}
