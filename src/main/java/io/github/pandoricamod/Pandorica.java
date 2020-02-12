package io.github.pandoricamod;

import io.github.pandoricamod.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pandorica implements ModInitializer {
    public static final String MOD_ID = "pandorica";
    public static final String MOD_PREFIX = MOD_ID + ":";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "item_group"), () -> new ItemStack(PandoricaBlocks.ERUPTION_BLOCK));
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[Pandorica] Loaded");

        new PandoricaItems();
        new PandoricaBlocks();
        new PandoricaEntities();

        PandoricaBlocks.addAdditionalBlockProperties();
        PandoricaWorldGen.addFeatures();
    }
}
