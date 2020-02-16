package io.github.pandoricamod.entity.magmator;

import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.EnumSet;
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
        targetSelector.add(1, new RevengeGoal(this));
        targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
        goalSelector.add(2, new PounceAtTargetGoal(this, 0.85D, 3.5D));
        goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
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

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    static class PounceAtTargetGoal extends Goal {
        private final MobEntity mob;
        private LivingEntity target;
        private final double velocityMultiplier;
        private final double distanceToStart;

        public PounceAtTargetGoal(MobEntity mob, double velocityMultiplier, double distanceToStart) {
            this.mob = mob;
            this.velocityMultiplier = velocityMultiplier;
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
                    if (mob.squaredDistanceTo(target) <= distanceToStart * distanceToStart) {
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

            mob.setVelocity(vec3d2.x, Math.min(mob.squaredDistanceTo(target), velocityMultiplier), vec3d2.z);
            spawnPoof();
        }

        public void stop() {
            spawnPoof();
            if (mob.squaredDistanceTo(target) <= distanceToStart * distanceToStart) {
                damageTarget();
            }
        }

        public void damageTarget() {
            mob.swingHand(Hand.MAIN_HAND);
            mob.tryAttack(target);
        }

        private void spawnPoof() {
            Random random = new Random();
            if (mob.world.isClient) {
                for (int i = 0; i < 20; ++i) {
                    double d = random.nextGaussian() * 0.02D;
                    double e = random.nextGaussian() * 0.02D;
                    double f = random.nextGaussian() * 0.02D;
                    mob.world.addParticle(ParticleTypes.POOF, mob.offsetX(1.0D) - d * 10.0D, mob.getRandomBodyY() - e * 10.0D, mob.getParticleZ(1.0D) - f * 10.0D, d, e, f);
                }
            }
        }
    }
}
