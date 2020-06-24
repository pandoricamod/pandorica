package io.github.pandoricamod.init;

import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class PandoricaWorldGen {
    public PandoricaWorldGen() {
        for (Biome biome : Registry.BIOME) {
            if (biome.getCategory() == Biome.Category.NETHER) {
                addToMagmaDecorator(biome, PandoricaBlocks.CRUMBLED_BASALT);
            }
        }
    }

    public static void addToMagmaDecorator(Biome biome, Block block) {
        biome.addFeature(
            GenerationStep.Feature.UNDERGROUND_DECORATION,
            Feature.ORE.configure (
                new OreFeatureConfig (
                    OreFeatureConfig.Target.NETHERRACK,
                    block.getDefaultState(),
                    33
                )
            )
            .createDecoratedFeature (
                Decorator.MAGMA.configure (
                    new CountDecoratorConfig(4)
                )
            )
        );
    }
}
