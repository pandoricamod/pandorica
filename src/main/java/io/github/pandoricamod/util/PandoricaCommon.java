package io.github.pandoricamod.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;

public class PandoricaCommon {
    public static class StaticMeleeAttackGoal {
        public static boolean canStart(MobEntity mob, long lastUpdateTime, Path path, double squaredMaxAttackDistance) {
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
                        return squaredMaxAttackDistance >= mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
                    }
                }
            }
        }

        public static boolean shouldContinue(MobEntity mob, boolean pauseWhenMobIdle) {
            LivingEntity target = mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!pauseWhenMobIdle) {
                return !mob.getNavigation().isIdle();
            } else if (!mob.isInWalkTargetRange(new BlockPos(target.getPos()))) {
                return false;
            } else {
                return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
            }
        }

        public static void start(MobEntity mob, Path path, double speed, int updateCountdownTicks) {
            mob.getNavigation().startMovingAlong(path, speed);
            mob.setAttacking(true);
            updateCountdownTicks = 0;
        }

        public static void stop(MobEntity mob) {
            LivingEntity target = mob.getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
                mob.setTarget(null);
            }

            mob.setAttacking(false);
            mob.getNavigation().stop();
        }

        @SuppressWarnings("all")
        public static void tick(MobEntity mob, int updateCountdownTicks, boolean pauseWhenMobIdle, double targetX, double targetY, double targetZ, double speed, int ticksUntilAttack) {
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
        }

        public static double getSquaredMaxAttackDistance(double maxAttackDistance) {
            return maxAttackDistance * maxAttackDistance;
        }
    }
}
