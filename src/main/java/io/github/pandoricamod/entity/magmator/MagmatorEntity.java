package io.github.pandoricamod.entity.magmator;

import io.github.pandoricamod.init.QMSoundEvents;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class MagmatorEntity extends HostileEntity {
    private static final TrackedData<Boolean> SADDLED;

    public MagmatorEntity(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    public static boolean method_24349(EntityType<MagmatorEntity> entityType, IWorld world, SpawnType spawnType, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && !world.getBlockState(pos.down()).matches(BlockTags.NYLIUM);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SADDLED, false);
    }
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putBoolean("Saddle", this.isSaddled());
    }
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setSaddled(tag.getBoolean("Saddle"));
    }

    protected SoundEvent getAmbientSound() {
        return QMSoundEvents.ENTITY_MAGMATOR_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource source) {
        return QMSoundEvents.ENTITY_MAGMATOR_HURT;
    }
    protected SoundEvent getDeathSound() {
        return QMSoundEvents.ENTITY_MAGMATOR_DEATH;
    }
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(QMSoundEvents.ENTITY_MAGMATOR_STEP, 0.15F, 1.0F);
    }

    public boolean interactMob(PlayerEntity player, Hand hand) {
        if (super.interactMob(player, hand)) {
            return true;
        } else {
            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.getItem() == Items.NAME_TAG) {
                itemStack.useOnEntity(player, this, hand);
                return true;
            } else if (this.isSaddled() && !this.hasPassengers()) {
                if (!this.world.isClient) {
                    player.startRiding(this);
                }

                return true;
            } else {
                return itemStack.getItem() == Items.SADDLE && itemStack.useOnEntity(player, this, hand);
            }
        }
    }
    protected void dropInventory() {
        super.dropInventory();
        if (this.isSaddled()) {
            this.dropItem(Items.SADDLE);
        }

    }
    public boolean isSaddled() {
        return this.dataTracker.get(SADDLED);
    }
    public void setSaddled(boolean saddled) {
        if (saddled) {
            this.dataTracker.set(SADDLED, true);
        } else {
            this.dataTracker.set(SADDLED, false);
        }

    }
    @Nullable
    public Entity getPrimaryPassenger() {
        return this.getPassengerList().isEmpty() ? null : this.getPassengerList().get(0);
    }
    public boolean canBeControlledByRider() {
        Entity entity = this.getPrimaryPassenger();
        if (!(entity instanceof PlayerEntity)) {
            return false;
        } else {
            return true;
        }
    }

    static {
        SADDLED = DataTracker.registerData(MagmatorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
