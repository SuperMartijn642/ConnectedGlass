package com.supermartijn642.connectedglass.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
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
        CGTagProvider.blockTags.forEach(this::addAll);
    }

    private void addAll(Tag.Named<Block> blockTag, List<Block> blocks){
        TagAppender<Block> tag = this.tag(blockTag);
        blocks.forEach(tag::add);
    }
}
