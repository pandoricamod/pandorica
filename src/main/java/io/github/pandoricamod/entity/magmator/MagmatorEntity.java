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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;
import java.util.Random;

public class MagmatorEntity extends HostileEntity {
    public static final float pounceVelocity = 0.75F;

    public MagmatorEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    protected void initGoals() {
        this.targetSelector.add(1, new FollowTargetGoal(this, PlayerEntity.class, true));
        this.goalSelector.add(2, new MagmatorPounceGoal(this, pounceVelocity));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.add(4, new RevengeGoal(this));
        this.goalSelector.add(5, new GoToEntityTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    public static boolean canSpawn(EntityType<MagmatorEntity> type, IWorld world, SpawnType spawnType, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && !world.getBlockState(pos.down()).matches(BlockTags.NYLIUM);
    }
    public boolean canSpawn(WorldView world) {
        return world.intersectsEntities(this) && !world.containsFluid(this.getBoundingBox());
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
        this.playSound(PandoricaSoundEvents.ENTITY_MAGMATOR_STEP, 0.15F, 1.0F);
    }

    static class MagmatorPounceGoal extends Goal {
        private final MobEntity mob;
        private LivingEntity target;
        private final float velocity;

        public MagmatorPounceGoal(MobEntity rmob, float velocity) {
            this.mob = rmob;
            this.velocity = velocity;
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
                    if (d >= 20.0D && d <= 32.0D) {
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

            mob.setVelocity(vec3d2.x, velocity, vec3d2.z);

            mob.getEntityWorld().addParticle(ParticleTypes.POOF, mob.getX(), mob.getY(), mob.getZ(), 0.3D, 0.2D, 0.3D);
        }
    }
}
