package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

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
            type.blocks.forEach(this.blockTag(Tags.Blocks.GLASS_BLOCKS)::add);
            type.blocks.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_BLOCKS)::add);
            if(type.isTinted){
                type.blocks.forEach(this.blockTag(Tags.Blocks.GLASS_BLOCKS_TINTED)::add);
                type.blocks.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_BLOCKS_TINTED)::add);
            }else{
                type.blocks.forEach(this.blockTag(Tags.Blocks.GLASS_BLOCKS_CHEAP)::add);
                type.blocks.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_BLOCKS_CHEAP)::add);
                this.blockTag(Tags.Blocks.GLASS_BLOCKS_COLORLESS).add(type.block);
                this.itemTag(Tags.Items.GLASS_BLOCKS_COLORLESS).add(type.block.asItem());
            }
            if(type.hasPanes){
                type.panes.forEach(this.blockTag(Tags.Blocks.GLASS_PANES)::add);
                type.panes.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_PANES)::add);
                if(!type.isTinted){
                    this.blockTag(Tags.Blocks.GLASS_PANES_COLORLESS).add(type.pane);
                    this.itemTag(Tags.Items.GLASS_PANES_COLORLESS).add(type.pane.asItem());
                }
            }
        }

        // Impermeable tag
        TagBuilder<Block> impermeable = this.blockTag(BlockTags.IMPERMEABLE);
        for(CGGlassType type : CGGlassType.values())
            type.blocks.forEach(impermeable::add);
    }
}
