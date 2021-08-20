package com.supermartijn642.connectedglass.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

/**
 * Created 6/23/2020 by SuperMartijn642
 */
public class CGItemTagProvider extends ItemTagsProvider {

    public CGItemTagProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper){
        super(generatorIn, blockTagsProvider, "connectedglass", existingFileHelper);
    }

    @Override
    protected void addTags(){
        CGTagProvider.itemTags.forEach(this::addAll);
    }

    private void addAll(Tag.Named<Item> itemTag, List<Block> blocks){
        TagAppender<Item> itemBuilder = this.tag(itemTag);
        blocks.forEach(block -> itemBuilder.add(block.asItem()));
    }
}
