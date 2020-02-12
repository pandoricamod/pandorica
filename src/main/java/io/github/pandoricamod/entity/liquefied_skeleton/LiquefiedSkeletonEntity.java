package io.github.pandoricamod.entity.liquefied_skeleton;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class LiquefiedSkeletonEntity extends WitherSkeletonEntity {
    public LiquefiedSkeletonEntity(EntityType<? extends WitherSkeletonEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    protected void initEquipment(LocalDifficulty localDifficulty_1) {}

    public void tickMovement() {
        if (this.isAlive() && !this.isInLava()) {
            this.damage(DamageSource.DROWN, 1);
        }

        super.tickMovement();
    }
}
