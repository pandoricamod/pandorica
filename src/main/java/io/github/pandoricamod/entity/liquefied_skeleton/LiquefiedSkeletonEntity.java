package io.github.pandoricamod.entity.liquefied_skeleton;

import io.github.pandoricamod.init.PandoricaSoundEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class LiquefiedSkeletonEntity extends WitherSkeletonEntity {
    public LiquefiedSkeletonEntity(EntityType<? extends WitherSkeletonEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_DEATH;
    }

    SoundEvent getStepSound() {
        return PandoricaSoundEvents.ENTITY_LIQUEFIED_SKELETON_STEP;
    }

    protected void initEquipment(LocalDifficulty localDifficulty_1) {}

    public void tickMovement() {
        if (this.isAlive() && !this.isInLava()) {
            this.damage(DamageSource.DROWN, 1);
        }

        super.tickMovement();
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = (float)this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue();
        float g = (float)this.getAttributeInstance(EntityAttributes.ATTACK_KNOCKBACK).getValue();
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity)target).getGroup());
            g += (float)EnchantmentHelper.getKnockback(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            target.setOnFireFor(i * 4);
        }

        boolean bl = target.damage(DamageSource.mob(this), f);
        if (bl) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity)target).takeKnockback(g * 0.5F, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
                this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }

            if (target instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)target;
                this.method_24521(playerEntity, this.getMainHandStack(), playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
            }

            this.dealDamage(this, target);
            this.onAttacking(target);
        }

        return bl;
    }

    private void method_24521(PlayerEntity playerEntity, ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem && itemStack2.getItem() == Items.SHIELD) {
            float f = 0.25F + (float)EnchantmentHelper.getEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                playerEntity.getItemCooldownManager().set(Items.SHIELD, 100);
                this.world.sendEntityStatus(playerEntity, (byte)30);
            }
        }
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {}
}
