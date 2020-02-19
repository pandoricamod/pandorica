package io.github.pandoricamod.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class PandoricaDamageSource extends DamageSource {
    protected PandoricaDamageSource(String name) {
        super(name);
    }

    public static DamageSource ambush(LivingEntity attacker) {
        return new EntityDamageSource("ambush", attacker);
    }
}
