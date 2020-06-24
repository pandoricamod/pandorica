package io.github.pandoricamod.entity.liquefied_skeleton;

import io.github.pandoricamod.init.PandoricaDamageSources;
import io.github.pandoricamod.init.PandoricaSoundEvents;
import io.github.pandoricamod.util.PandoricaCommon;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

public class LiquefiedSkeletonEntity extends WitherSkeletonEntity {
    public static final String id = "liquefied_skeleton";
    private int outOfLavaDamageTick;

    public LiquefiedSkeletonEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);

        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
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

    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.65D));

        goalSelector.add(0, new AmbushGoal(this, 1D, 16.0D, 4.0D, 1.0D, true));

        super.initGoals();
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
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        BlockPos pos = new BlockPos(getX(), getY(), getZ());
        return world.getBlockState(pos.down()).isOf(Blocks.LAVA) && !world.isAir(pos);
    }
    @Override
    public boolean canSpawn(WorldView world) {
        return world.intersectsEntities(this);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    protected void initEquipment(LocalDifficulty difficulty) {}

    protected void mobTick() {
        super.mobTick();

        if (isAlive() && !isInLava()) {
            outOfLavaDamageTick++;
            if (outOfLavaDamageTick >= 20) {
                damage(DamageSource.DROWN, 2);
                outOfLavaDamageTick = 0;
            }


            Random random = new Random();
                int rand = random.nextInt(2) * 2 - 1;
            double delta = 0.25;
            int count = 4;
            for (int i = 0; i < count; i++) {
                double x = getX() + 0.5D + 0.25D * (double)rand;
                double y = (float)getY() + random.nextFloat();
                double z = getZ() + 0.5D + 0.25D * (double)rand;

                world.addParticle(ParticleTypes.LAVA, x, y, z, delta, delta, delta);
            }
        } else {
            outOfLavaDamageTick = 0;
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = (float)Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE))
                .getValue();
        float g = (float)Objects.requireNonNull(getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK))
                .getValue();
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(getMainHandStack(), ((LivingEntity)target).getGroup());
            g += (float)EnchantmentHelper.getKnockback(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            target.setOnFireFor(i * 4);
        }

        boolean bl = target.damage(PandoricaDamageSources.ambush(this), f);
        if (bl) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5F, MathHelper.sin(yaw * 0.017453292F),
                        -MathHelper.cos(yaw * 0.017453292F));
                setVelocity(getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }

            if (target instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)target;
                shieldBlockCooldownCheck(playerEntity, getMainHandStack(),
                        playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
            }

            dealDamage(this, target);
            onAttacking(target);
        }

        return bl;
    }

    private void shieldBlockCooldownCheck(PlayerEntity playerEntity, ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem
                && itemStack2.getItem() == Items.SHIELD) {
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
                return PandoricaCommon.StaticMeleeAttackGoal.canStart(mob, lastUpdateTime, path,
                        getSquaredMaxAttackDistance());
            } else {
                return false;
            }
        }

        public boolean shouldContinue() {
            Vec3d velocity = mob.getVelocity();
            if (!mob.isInLava()) {
                return false;
            } else if (!(mob.isOnGround() && (velocity.getX() == 0 && velocity.getZ() == 0)) || velocity.getY() >= 0.3d) {
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

            Vec3d velocity = mob.getVelocity();
            if ((mob.isOnGround() && mob.squaredDistanceTo(target) <= distanceToStart * distanceToStart && mob.getY() < target.getY()) || (velocity.getX() == 0 && velocity.getY() < 0 && velocity.getZ() == 0)) {
                ambush(target);
            }
        }

        public void ambush(LivingEntity target) {
            Vec3d vec3d = mob.getVelocity();
            Vec3d vec3d2 = new Vec3d(target.getX() - mob.getX(), 0.0D, target.getZ() - mob.getZ());
            if (vec3d2.lengthSquared() > 1.0E-7D) {
                vec3d2 = vec3d2.normalize().multiply(0.4D).add(vec3d.multiply(0.2D));
            }

            Vec3d rotation = mob.getRotationVector();
            mob.setVelocity(rotation.x * velocityModifier * 2,
                    Math.min(Math.sqrt(mob.squaredDistanceTo(target)), 3.9D) * velocityModifier,
                    rotation.z * velocityModifier * 2);

            mob.swingHand(Hand.MAIN_HAND);
            target.damage(PandoricaDamageSources.ambush(mob), (float)mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() / 2);

            mob.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
        }

        protected double getSquaredMaxAttackDistance() {
            return PandoricaCommon.StaticMeleeAttackGoal.getSquaredMaxAttackDistance(maxAttackDistance);
        }
    }
}
