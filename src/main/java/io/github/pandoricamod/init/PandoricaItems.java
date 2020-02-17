package io.github.pandoricamod.init;

import co.origamigames.sheet.SheetLib;
import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.entity.liquefied_skeleton.LiquefiedSkeletonEntity;
import io.github.pandoricamod.entity.magmator.MagmatorEntity;
import io.github.pandoricamod.item.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class PandoricaItems {
    public static final Item BASALT_DUST = register("basalt_dust");
    public static final Item CRUSTED_MAGMA = register("crusted_magma");
    public static final Item LIQUEFIED_BONE = register("liquefied_bone");
    public static final Item FUNGI_SOUP = register(FungiSoupItem.id, new FungiSoupItem(FungiSoupItem.item));

    public static final Item LIQUEFIED_SKELETON_SPAWN_EGG = register(LiquefiedSkeletonEntity.id, PandoricaEntities.LIQUEFIED_SKELETON, 15080197, 15080197);
    public static final Item MAGMATOR_SPAWN_EGG = register(MagmatorEntity.id, PandoricaEntities.MAGMATOR, 13246217, 8593161);

    private static Item register(String id) {
        return SheetLib.item(Pandorica.MOD_ID, id, Pandorica.ITEM_GROUP, 64);
    }
    private static Item register(String id, Item item) {
        return SheetLib.item(Pandorica.MOD_ID, id, item);
    }
    @SuppressWarnings("rawtypes")
    private static Item register(String entity_id, EntityType entity, int primaryColor, int secondaryColor) {
        return SheetLib.spawnEggItem(Pandorica.MOD_ID, entity_id, Pandorica.ITEM_GROUP, 64, entity, primaryColor, secondaryColor);
    }
}
