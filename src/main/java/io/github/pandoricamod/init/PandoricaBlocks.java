package io.github.pandoricamod.init;

import co.origamigames.sheet.SheetLib;
import co.origamigames.sheet.block.helpers.BlockWithDecor;
import co.origamigames.sheet.block.helpers.WoodBlocks;
import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.block.*;
import io.github.pandoricamod.block.soul_bricks.*;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;

@SuppressWarnings("unused")
public class PandoricaBlocks {
    public static final BlockWithDecor SOUL_BRICKS = new BlockWithDecor(BlockWithDecorInfo.SOUL_BRICKS);
    public static final WoodBlocks WEEPING_WOOD = new WoodBlocks(WoodBlocksInfo.WEEPING);

    public static final Block CRUMBLED_BASALT = register("crumbled_basalt", new FallingBlock(AbstractBlock.Settings.copy(Blocks.SAND)));

    public static final Block MAGMA_CREAM_BLOCK = register(MagmaCreamBlock.id, new MagmaCreamBlock(AbstractBlock.Settings.of(Material.CLAY, MaterialColor.NETHER).lightLevel((blockStatex) -> 8).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME)));
    public static final Block MAGMATIC_NETHERRACK = register("magmatic_netherrack", new MagmaBlock(AbstractBlock.Settings.copy(Blocks.MAGMA_BLOCK)));
    public static final Block ERUPTION_BLOCK = register(EruptionBlock.id, new EruptionBlock(AbstractBlock.Settings.copy(Blocks.NETHERRACK)));

    private static Block register(String id, Block block) {
        return SheetLib.block(Pandorica.MOD_ID, id, Pandorica.ITEM_GROUP, block);
    }
    private static Block registerCopiedBlock(String id, Block block) {
        return SheetLib.copiedBlock(Pandorica.MOD_ID, id, Pandorica.ITEM_GROUP, block);
    }

    public static class BlockWithDecorInfo {
        public static final BlockWithDecor.Info SOUL_BRICKS = new BlockWithDecor.Info();
        public static BlockWithDecor.Info[] BLOCK_WITH_DECOR_INFO = { SOUL_BRICKS };

        static {
            SOUL_BRICKS.block_id = "soul_brick";
            SOUL_BRICKS.isPlural = true;
            SOUL_BRICKS.isFlammable = false;
            SOUL_BRICKS.base = new SoulBricksBlock();
            SOUL_BRICKS.slab = new SoulBrickSlabBlock();
            SOUL_BRICKS.stairs = new SoulBrickStairsBlock();
            SOUL_BRICKS.wall = new SoulBrickWallBlock();

            for (BlockWithDecor.Info blockWithDecorInfo : BLOCK_WITH_DECOR_INFO) {
                blockWithDecorInfo.mod_id = Pandorica.MOD_ID;
                blockWithDecorInfo.item_group = Pandorica.ITEM_GROUP;
            }
        }
    }
    public static class WoodBlocksInfo {
        public static final WoodBlocks.Info WEEPING = new WoodBlocks.Info();
        public static WoodBlocks.Info[] WOOD_BLOCKS_INFO = { WEEPING };

        static {
            WEEPING.wood_id = "weeping";
            WEEPING.hasLeaves = false;
            WEEPING.isFlammable = false;
            WEEPING.topMaterialColor = MaterialColor.RED;
            WEEPING.sideMaterialColor = MaterialColor.RED_TERRACOTTA;
            WEEPING.sideMaterialColorStripped = MaterialColor.PINK_TERRACOTTA;

            for (WoodBlocks.Info woodBlockInfo : WOOD_BLOCKS_INFO) {
                woodBlockInfo.mod_id = Pandorica.MOD_ID;
                woodBlockInfo.item_group = Pandorica.ITEM_GROUP;
            }
        }
    }
}
