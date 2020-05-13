package io.github.pandoricamod.init;

import co.origamigames.sheet.SheetLib;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class PandoricaWorldGen {
    public static void addFeatures() {
        SheetLib.addToMagmaDecorator(PandoricaBlocks.CRUMBLED_BASALT, 35, 15, OreFeatureConfig.Target.NETHERRACK);
    }
}
