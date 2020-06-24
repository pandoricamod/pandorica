package io.github.pandoricamod.init;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class PandoricaDamageSources {
    public static DamageSource ambush(LivingEntity attacker) {
        return new EntityDamageSource("ambush", attacker);
    }
}
