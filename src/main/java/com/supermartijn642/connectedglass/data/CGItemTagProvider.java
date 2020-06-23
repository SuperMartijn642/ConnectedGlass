package com.supermartijn642.connectedglass.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;
import java.util.List;

/**
 * Created 6/23/2020 by SuperMartijn642
 */
public class CGItemTagProvider extends TagsProvider<Item> {

    public CGItemTagProvider(DataGenerator generatorIn, Registry<Item> registryIn){
        super(generatorIn, registryIn);
    }

    @Override
    protected void registerTags(){
        CGTagProvider.itemTags.forEach(this::addAll);
    }

    private void addAll(Tag<Item> itemTag, List<Block> blocks){
        Tag.Builder<Item> itemBuilder = getBuilder(itemTag).replace(false);
        blocks.forEach(block -> itemBuilder.add(block.asItem()));
        itemBuilder.build(itemTag.getId());
    }

    @Override
    protected void setCollection(TagCollection colectionIn){
    }

    @Override
    protected Path makePath(ResourceLocation id){
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }

    @Override
    public String getName(){
        return "connectedglass:itemtags";
    }
}
