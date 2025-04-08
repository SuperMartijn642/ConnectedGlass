package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGColoredGlassBlock;
import com.supermartijn642.connectedglass.CGColoredPaneBlock;
import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;

/**
 * Created 5/26/2020 by SuperMartijn642
 */
public class CGTagGenerator extends TagGenerator {

    @SuppressWarnings("unchecked")
    private static final TagKey<Block>[] COLORED_BLOCK_TAGS = new TagKey[]{
        Tags.Blocks.DYED_WHITE, Tags.Blocks.DYED_ORANGE, Tags.Blocks.DYED_MAGENTA, Tags.Blocks.DYED_LIGHT_BLUE, Tags.Blocks.DYED_YELLOW, Tags.Blocks.DYED_LIME, Tags.Blocks.DYED_PINK, Tags.Blocks.DYED_GRAY, Tags.Blocks.DYED_LIGHT_GRAY, Tags.Blocks.DYED_CYAN, Tags.Blocks.DYED_PURPLE, Tags.Blocks.DYED_BLUE, Tags.Blocks.DYED_BROWN, Tags.Blocks.DYED_GREEN, Tags.Blocks.DYED_RED, Tags.Blocks.DYED_BLACK
    };
    @SuppressWarnings("unchecked")
    private static final TagKey<Item>[] COLORED_ITEM_TAGS = new TagKey[]{
        Tags.Items.DYED_WHITE, Tags.Items.DYED_ORANGE, Tags.Items.DYED_MAGENTA, Tags.Items.DYED_LIGHT_BLUE, Tags.Items.DYED_YELLOW, Tags.Items.DYED_LIME, Tags.Items.DYED_PINK, Tags.Items.DYED_GRAY, Tags.Items.DYED_LIGHT_GRAY, Tags.Items.DYED_CYAN, Tags.Items.DYED_PURPLE, Tags.Items.DYED_BLUE, Tags.Items.DYED_BROWN, Tags.Items.DYED_GREEN, Tags.Items.DYED_RED, Tags.Items.DYED_BLACK
    };

    public CGTagGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        ArrayList<Block> glass = new ArrayList<>();
        ArrayList<CGColoredGlassBlock> glassColored = new ArrayList<>();
        ArrayList<Block> glassColorless = new ArrayList<>();
        ArrayList<Block> glassTinted = new ArrayList<>();
        ArrayList<Block> panes = new ArrayList<>();
        ArrayList<CGColoredPaneBlock> panesColored = new ArrayList<>();
        ArrayList<Block> panesColorless = new ArrayList<>();
        ArrayList<Block> panesTinted = new ArrayList<>();

        for(CGGlassType type : CGGlassType.values()){
            glass.addAll(type.blocks);
            if(type.isTinted)
                glassTinted.addAll(type.blocks);
            else{
                glassColored.addAll(type.colored_blocks.values());
                glassColorless.add(type.block);
            }
            if(type.hasPanes){
                panes.addAll(type.panes);
                if(type.isTinted)
                    panesTinted.addAll(type.panes);
                else{
                    panesColored.addAll(type.colored_panes.values());
                    panesColorless.add(type.pane);
                }
            }
        }

        glass.forEach(this.blockTag(Tags.Blocks.GLASS_BLOCKS)::add);
        glass.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_BLOCKS)::add);
        glassColored.forEach(b -> {
            this.blockTag(COLORED_BLOCK_TAGS[b.getColor().getId()]).add(b);
            this.itemTag(COLORED_ITEM_TAGS[b.getColor().getId()]).add(b.asItem());
        });
        glassColorless.forEach(this.blockTag(Tags.Blocks.GLASS_BLOCKS_COLORLESS)::add);
        glassColorless.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_BLOCKS_COLORLESS)::add);
        glassTinted.forEach(this.blockTag(Tags.Blocks.GLASS_BLOCKS_TINTED)::add);
        glassTinted.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_BLOCKS_TINTED)::add);
        panes.forEach(this.blockTag(Tags.Blocks.GLASS_PANES)::add);
        panes.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_PANES)::add);
        panesColored.forEach(b -> {
            this.blockTag(COLORED_BLOCK_TAGS[b.getColor().getId()]).add(b);
            this.itemTag(COLORED_ITEM_TAGS[b.getColor().getId()]).add(b.asItem());
        });
        panesColorless.forEach(this.blockTag(Tags.Blocks.GLASS_PANES_COLORLESS)::add);
        panesColorless.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_PANES_COLORLESS)::add);
        panesTinted.forEach(this.blockTag("c", "glass_panes/tinted")::add);
        panesTinted.stream().map(Block::asItem).forEach(this.itemTag("c", "glass_panes/tinted")::add);

        // Impermeable tag
        TagBuilder<Block> impermeable = this.blockTag(BlockTags.IMPERMEABLE);
        for(CGGlassType type : CGGlassType.values())
            type.blocks.forEach(impermeable::add);
    }
}
