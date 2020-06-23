package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PandoricaItems {
    public static final Item BASALT_DUST = register("basalt_dust");
    public static final Item CRUSTED_MAGMA = register("crusted_magma");

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Pandorica.MOD_ID, id), item);
    }

    private static Item register(String id) {
        return register(id, new Item(new Item.Settings().maxCount(64).group(Pandorica.ITEM_GROUP)));
    }
}
