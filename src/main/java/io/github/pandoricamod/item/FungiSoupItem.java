package io.github.pandoricamod.item;

import io.github.pandoricamod.Pandorica;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class FungiSoupItem extends Item {
    public static final String id = "fungi_soup";

    public FungiSoupItem() {
        super(
            new Item.Settings()
                .maxCount(1)
                .group(Pandorica.ITEM_GROUP)
                .food(
                    new FoodComponent.Builder()
                        .hunger(6)
                        .saturationModifier(0.3F)
                        .alwaysEdible()
                        .statusEffect(
                            new StatusEffectInstance(
                                StatusEffects.REGENERATION,
                                200,
                                1
                            ), 1F)
                        .build()
                )
        );
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        return user instanceof PlayerEntity && ((PlayerEntity) user).abilities.creativeMode ? itemStack
                : new ItemStack(Items.BOWL);
    }
}
