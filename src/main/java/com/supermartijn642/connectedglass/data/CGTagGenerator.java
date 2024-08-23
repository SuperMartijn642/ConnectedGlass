package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

/**
 * Created 5/26/2020 by SuperMartijn642
 */
public class CGTagGenerator extends TagGenerator {

    public CGTagGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        for(CGGlassType type : CGGlassType.values()){
            type.blocks.forEach(this.blockTag(ConventionalBlockTags.GLASS_BLOCKS)::add);
            type.blocks.stream().map(Block::asItem).forEach(this.itemTag(ConventionalItemTags.GLASS_BLOCKS)::add);
            if(type.hasPanes){
                type.panes.forEach(this.blockTag(ConventionalBlockTags.GLASS_PANES)::add);
                type.panes.stream().map(Block::asItem).forEach(this.itemTag(ConventionalItemTags.GLASS_PANES)::add);
            }
        }

        // Impermeable tag
        TagBuilder<Block> impermeable = this.blockTag(BlockTags.IMPERMEABLE);
        for(CGGlassType type : CGGlassType.values())
            type.blocks.forEach(impermeable::add);
    }
}
