package io.github.pandoricamod.init;

import co.origamigames.sheet.SheetLib;
import co.origamigames.sheet.block.*;
import io.github.pandoricamod.Pandorica;
import io.github.pandoricamod.block.*;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.MagmaBlock;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.PressurePlateBlock.ActivationRule;

public class PandoricaBlocks {
    // define blocks
    public static final Block CRUMBLED_BASALT = register("crumbled_basalt", new FallingBlock(FabricBlockSettings.copy(Blocks.SAND).breakByTool(FabricToolTags.SHOVELS).build()));
    public static final Block NETHER_GOLD_ORE = registerCopiedBlock("nether_gold_ore", Blocks.GOLD_ORE);

    public static final Block WEEPING_BUTTON = register("weeping_button", new PublicWoodButtonBlock(FabricBlockSettings.copy(Blocks.OAK_BUTTON).build()));
    public static final Block WEEPING_DOOR = register("weeping_door", new PublicDoorBlock(FabricBlockSettings.copy(Blocks.OAK_DOOR).build()));
    public static final Block WEEPING_FENCE = register("weeping_fence", new FenceBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE).build()));
    public static final Block WEEPING_FENCE_GATE = register("weeping_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(Blocks.OAK_FENCE_GATE).build()));
    public static final Block WEEPING_LOG = register("weeping_log", new LogBlock(MaterialColor.WOOD, FabricBlockSettings.copy(Blocks.OAK_LOG).build()));
    public static final Block STRIPPED_WEEPING_LOG = register("stripped_weeping_log", new LogBlock(MaterialColor.WOOD, FabricBlockSettings.copy(Blocks.STRIPPED_OAK_LOG).build()));
    public static final Block WEEPING_PLANKS = registerCopiedBlock("weeping_planks", Blocks.OAK_PLANKS);
    public static final Block WEEPING_PRESSURE_PLATE = register("weeping_pressure_plate", new PublicPressurePlateBlock(ActivationRule.EVERYTHING, FabricBlockSettings.copy(Blocks.OAK_PRESSURE_PLATE).build()));
//    public static final Block WEEPING_SIGN = register("weeping_sign", new SignBlock(FabricBlockSettings.copy(Blocks.OAK_SIGN).build(), SignType.OAK));
//    public static final Block WEEPING_WALL_SIGN = register("weeping_wall_sign", new WallSignBlock(FabricBlockSettings.copy(Blocks.OAK_WALL_SIGN).build(), SignType.OAK));
    public static final Block WEEPING_STAIRS = register("weeping_stairs", new PublicStairsBlock(WEEPING_PLANKS.getDefaultState(), FabricBlockSettings.copy(Blocks.OAK_STAIRS).build()));
    public static final Block WEEPING_SLAB = register("weeping_slab", new SlabBlock(FabricBlockSettings.copy(Blocks.OAK_SLAB).build()));
    public static final Block WEEPING_TRAPDOOR = register("weeping_trapdoor", new PublicTrapdoorBlock(FabricBlockSettings.copy(Blocks.OAK_TRAPDOOR).build()));
    public static final Block WEEPING_WOOD = register("weeping_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD).build()));
    public static final Block STRIPPED_WEEPING_WOOD = register("stripped_weeping_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_WOOD).build()));

    public static final Block ERUPTION_BLOCK = register("eruption_block", new EruptionBlock(FabricBlockSettings.copy(Blocks.NETHERRACK).build()));
    public static final Block MAGMATIC_NETHERRACK = register("magmatic_netherrack", new MagmaBlock(FabricBlockSettings.copy(Blocks.NETHERRACK).build()));

//    public static final Block WAILING_LOG = register("wailing_log", new LogBlock(MaterialColor.WOOD, FabricBlockSettings.copy(Blocks.OAK_LOG).build()));
//    public static final Block STRIPPED_WAILING_LOG = register("stripped_wailing_log", new LogBlock(MaterialColor.WOOD, FabricBlockSettings.copy(Blocks.STRIPPED_OAK_LOG).build()));
//    public static final Block WAILING_PLANKS = registerCopiedBlock("wailing_planks", Blocks.OAK_PLANKS);
//    public static final Block WAILING_WOOD = register("wailing_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_WOOD).build()));
//    public static final Block STRIPPED_WAILING_WOOD = register("stripped_wailing_wood", new PillarBlock(FabricBlockSettings.copy(Blocks.STRIPPED_OAK_WOOD).build()));

    public static void addAdditionalBlockProperties() {
        addStrippingFunctionality(WEEPING_LOG, STRIPPED_WEEPING_LOG);
        addStrippingFunctionality(WEEPING_WOOD, STRIPPED_WEEPING_WOOD);
//        addStrippingFunctionality(WAILING_LOG, STRIPPED_WAILING_LOG);
//        addStrippingFunctionality(WAILING_WOOD, STRIPPED_WAILING_WOOD);
    }

    private static Block register(String id, Block block) {
        return SheetLib.block(Pandorica.MOD_ID, id, block, Pandorica.ITEM_GROUP);
    }
    private static Block registerCopiedBlock(String id, Block block) {
        return SheetLib.copiedBlock(Pandorica.MOD_ID, id, block, Pandorica.ITEM_GROUP);
    }

    private static void addStrippingFunctionality(Block blockToBeStripped, Block blockAfterStrip) {
        SheetLib.addStrippingFunctionality(blockToBeStripped, blockAfterStrip);
    }
}
