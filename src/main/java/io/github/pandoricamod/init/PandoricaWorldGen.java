package io.github.pandoricamod.init;

import co.origamigames.sheet.SheetLib;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class PandoricaWorldGen {
    public static void addFeatures() {
        addNetherOre(PandoricaBlocks.NETHER_GOLD_ORE, 8, 16, 10, 20, 128);
        addToMagmaDecorator(PandoricaBlocks.CRUMBLED_BASALT, 35, 15, OreFeatureConfig.Target.NETHERRACK);
    }

    private static void addNetherOre(Block block, int size, int count, int bottomOffset, int topOffset, int maxPerChunk) {
        SheetLib.addNetherOre(block, size, count, bottomOffset, topOffset, maxPerChunk);
    }
    private static void addToMagmaDecorator(Block block, int size, int count, OreFeatureConfig.Target target) {
        SheetLib.addToMagmaDecorator(block, size, count, target);
    }
}
