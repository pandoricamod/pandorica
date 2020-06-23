package io.github.pandoricamod;

import io.github.pandoricamod.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Pandorica implements ModInitializer {
    public static final String MOD_ID = "pandorica";
    public static final String MOD_NAME = "Pandorica";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(PandoricaBlocks.CRUMBLED_BASALT)
    );

    @Override
    public void onInitialize() {
        new PandoricaItems();
        new PandoricaBlocks();

        System.out.println("Loaded " + MOD_NAME);
    }
}
