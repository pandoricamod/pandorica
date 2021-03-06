package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public class PandoricaBlocks {
    public static final Block CRUMBLED_BASALT = register(
        "crumbled_basalt",
        new FallingBlock(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE_POWDER))
    );
    public static final Block MAGMA_CREAM_BLOCK = register(
        MagmaCreamBlock.id,
        new MagmaCreamBlock()
    );
    public static final Block MAGMATIC_NETHERRACK = register(
        "magmatic_netherrack",
        new MagmaBlock(AbstractBlock.Settings.copy(Blocks.MAGMA_BLOCK))
    );
    public static final Block ERUPTION_BLOCK = register(
        EruptionBlock.id,
        new EruptionBlock()
    );

    public static final Block POLISHED_NETHERRACK = register("polished_netherrack", new Block(AbstractBlock.Settings.of(Material.STONE, MaterialColor.STONE).requiresTool().strength(1.2F, 4.0F)));

    private static Block register(String id, Block block, boolean registerItem) {
        Identifier blockIdentifier = new Identifier(Pandorica.MOD_ID, id);

        Registry.register(Registry.BLOCK, blockIdentifier, block);
        if (registerItem)
            Registry.register(Registry.ITEM, blockIdentifier,
                    new BlockItem(block, new Item.Settings().maxCount(64).group(Pandorica.ITEM_GROUP)));

        return block;
    }
    private static Block register(String id, Block block) {
        return register(id, block, true);
    }
}
