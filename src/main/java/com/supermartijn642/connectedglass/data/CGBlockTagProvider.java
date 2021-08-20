package com.supermartijn642.connectedglass.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;
import java.util.List;

/**
 * Created 5/26/2020 by SuperMartijn642
 */
public class CGBlockTagProvider extends TagsProvider<Block> {

    public CGBlockTagProvider(DataGenerator generatorIn, Registry<Block> registryIn){
        super(generatorIn, registryIn);
    }

    @Override
    protected void addTags(){
        CGTagProvider.blockTags.forEach(this::addAll);
    }

    private void addAll(Tag<Block> blockTag, List<Block> blocks){
        Tag.Builder<Block> blockBuilder = tag(blockTag).replace(false);
        blocks.forEach(blockBuilder::add);
        blockBuilder.build(blockTag.getId());
    }

    @Override
    protected void useTags(TagCollection colectionIn){
    }

    @Override
    protected Path getPath(ResourceLocation id){
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    @Override
    public String getName(){
        return "connectedglass:blocktags";
    }
}
