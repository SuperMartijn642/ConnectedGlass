package com.supermartijn642.connectedglass.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;

import java.util.List;

/**
 * Created 5/26/2020 by SuperMartijn642
 */
public class CGBlockTagProvider extends BlockTagsProvider {

    public CGBlockTagProvider(DataGenerator generatorIn){
        super(generatorIn);
    }

    @Override
    protected void addTags(){
        CGTagProvider.BLOCK_TAGS.forEach(this::addAll);
    }

    private void addAll(Tag<Block> blockTag, List<Block> blocks){
        Tag.Builder<Block> blockBuilder = this.tag(blockTag);
        blocks.forEach(blockBuilder::add);
    }
}
