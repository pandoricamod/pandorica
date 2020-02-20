package io.github.pandoricamod.init;

import io.github.pandoricamod.Pandorica;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PandoricaTags {
    public static class blocks {
        public static final Tag<Block> ERUPTION_BLOCK_CONVERTIBLE = register("eruption_block_convertible");

        public static Tag<Block> register(String path) {
            return TagRegistry.block(new Identifier(Pandorica.MOD_ID, path));
        }
    }
}
