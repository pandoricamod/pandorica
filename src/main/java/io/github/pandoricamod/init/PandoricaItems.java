package io.github.pandoricamod.init;

import co.origamigames.sheet.SheetLib;
import io.github.pandoricamod.Pandorica;
import net.minecraft.item.Item;

public class PandoricaItems {

    public static final Item BASALT_DUST = register("basalt_dust");
    public static final Item CRUSTED_MAGMA = register("crusted_magma");
    public static final Item LIQUEFIED_BONE = register("liquefied_bone");

    private static Item register(String id) {
        return SheetLib.item(Pandorica.MOD_ID, id, Pandorica.ITEM_GROUP, 64);
    }
}
