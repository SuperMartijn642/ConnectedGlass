package com.supermartijn642.connectedglass.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
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
        CGTagProvider.ITEM_TAGS.forEach(this::addAll);
    }

    private void addAll(ITag.INamedTag<Item> itemTag, List<Block> blocks){
        Builder<Item> itemBuilder = this.tag(itemTag);
        blocks.forEach(block -> itemBuilder.add(block.asItem()));
    }
}
