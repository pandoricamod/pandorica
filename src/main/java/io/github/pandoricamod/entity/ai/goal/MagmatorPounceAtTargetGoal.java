package io.github.pandoricamod.entity.ai.goal;

import io.github.pandoricamod.entity.magmator.MagmatorEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class MagmatorPounceAtTargetGoal extends Goal {
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

    public MagmatorPounceAtTargetGoal(MobEntityWithAi mob, double velocityMultiplier, double distanceToStart, double maxAttackDistance, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.velocityMultiplier = velocityMultiplier;
        this.distanceToStart = distanceToStart;
        this.maxAttackDistance = maxAttackDistance;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        long l = mob.world.getTime();
        if (l - lastUpdateTime < 20L) {
            return false;
        } else {
            lastUpdateTime = l;
            LivingEntity target = mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                path = mob.getNavigation().findPathTo(target, 0);
                if (path != null) {
                    return true;
                } else {
                    return getSquaredMaxAttackDistance() >= mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
                }
            }
        }
    }

    public boolean shouldContinue() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!pauseWhenMobIdle) {
            return !mob.getNavigation().isIdle();
        } else if (!mob.isInWalkTargetRange(new BlockPos(target))) {
            return false;
        } else {
            return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
        }
    }

    public void start() {
        mob.getNavigation().startMovingAlong(path, speed);
        mob.setAttacking(true);
        updateCountdownTicks = 0;
    }

    public void stop() {
        LivingEntity target = mob.getTarget();
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
            mob.setTarget(null);
        }

        mob.setAttacking(false);
        mob.getNavigation().stop();
    }

    @SuppressWarnings("all")
    public void tick() {
        LivingEntity target = mob.getTarget();
        mob.getLookControl().lookAt(target, 30.0F, 30.0F);
        double d = mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
        --updateCountdownTicks;
        if ((pauseWhenMobIdle || mob.getVisibilityCache().canSee(target)) && updateCountdownTicks <= 0 && (targetX == 0.0D && targetY == 0.0D && targetZ == 0.0D || target.squaredDistanceTo(targetX, targetY, targetZ) >= 1.0D || mob.getRandom().nextFloat() < 0.05F)) {
            targetX = target.getX();
            targetY = target.getY();
            targetZ = target.getZ();
            updateCountdownTicks = 4 + mob.getRandom().nextInt(7);
            if (d > 1024.0D) {
                updateCountdownTicks += 10;
            } else if (d > 256.0D) {
                updateCountdownTicks += 5;
            }

            if (!mob.getNavigation().startMovingTo(target, speed)) {
                updateCountdownTicks += 15;
            }
        }

        ticksUntilAttack = Math.max(ticksUntilAttack - 1, 0);
        checkPounceStatus(mob, velocityMultiplier);
    }

    @SuppressWarnings("all")
    public void checkPounceStatus(MobEntity mob, double velocityMultiplier) {
        LivingEntity target = mob.getTarget();
        boolean isInRange = mob.squaredDistanceTo(mob.getTarget()) <= distanceToStart * distanceToStart;

        if (mob.onGround) {
            if (!isInRange) {
                canDamage = false;
            }

            if (hasJumped) {
                pounceDrop(target);
                hasJumped = false;
            }
            if (canDamage) {
                canDamage = false;
            } else if (isInRange) {
                pounceJump(target);
                hasJumped = true;
                canDamage = true;
            }
        }
    }

    private void pounceJump(LivingEntity target) {
        Vec3d vec3d = mob.getVelocity();
        Vec3d vec3d2 = new Vec3d(target.getX() - mob.getX(), 0.0D, target.getZ() - mob.getZ());
        if (vec3d2.lengthSquared() > 1.0E-7D) {
            vec3d2 = vec3d2.normalize().multiply(0.4D).add(vec3d.multiply(0.2D));
        }

        mob.setVelocity(vec3d2.x, Math.min(mob.squaredDistanceTo(target), velocityMultiplier), vec3d2.z);
    }

    private void pounceDrop(LivingEntity target) {
        // deal damage to target
        if (canDamage) {
            mob.swingHand(Hand.MAIN_HAND);
            mob.tryAttack(target);
        }

        // special cases
        MagmatorEntity.pounceBlockReplacement(mob);
    }

    protected double getSquaredMaxAttackDistance() {
        return maxAttackDistance * maxAttackDistance;
    }
}
