package com.supermartijn642.connectedglass.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

/**
 * Created 5/26/2020 by SuperMartijn642
 */
public class CGBlockTagProvider extends BlockTagsProvider {

    public CGBlockTagProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper){
        super(generatorIn, "connectedglass", existingFileHelper);
    }

    @Override
    protected void addTags(){
        CGTagProvider.BLOCK_TAGS.forEach(this::addAll);
    }

    private void addAll(ITag.INamedTag<Block> blockTag, List<Block> blocks){
        Builder<Block> tag = this.tag(blockTag);
        blocks.forEach(tag::add);
    }
}
