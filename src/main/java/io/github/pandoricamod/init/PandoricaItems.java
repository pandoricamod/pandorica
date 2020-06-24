package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.entity.liquefied_skeleton.LiquefiedSkeletonEntity;
import io.github.pandoricamod.entity.magmator.MagmatorEntity;
import io.github.pandoricamod.item.FungiSoupItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PandoricaItems {
    public static final Item BASALT_DUST = register("basalt_dust");
    public static final Item CRUSTED_MAGMA = register("crusted_magma");
    public static final Item LIQUEFIED_BONE = register("liquefied_bone");

    public static final Item FUNGI_SOUP = register(
        FungiSoupItem.id,
        new FungiSoupItem()
    );

    public static final Item LIQUEFIED_SKELETON_SPAWN_EGG = register(
        LiquefiedSkeletonEntity.id,
        PandoricaEntities.LIQUEFIED_SKELETON,
        15080197, 15080197);
    public static final Item MAGMATOR_SPAWN_EGG = register(
        MagmatorEntity.id,
        PandoricaEntities.MAGMATOR,
        13246217, 8593161);

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Pandorica.MOD_ID, id), item);
    }
    private static Item register(String id) {
        return register(id, new Item(new Item.Settings().maxCount(64).group(Pandorica.ITEM_GROUP)));
    }
    
    @SuppressWarnings("rawtypes")
    private static Item register(String entity_id, EntityType entity, int primaryColor, int secondaryColor) {
        return Registry.register(Registry.ITEM, Registry.ENTITY_TYPE.getId(entity) + "_spawn_egg", new SpawnEggItem(entity, primaryColor, secondaryColor, new Item.Settings().maxCount(64).group(Pandorica.ITEM_GROUP)));
    }
}
