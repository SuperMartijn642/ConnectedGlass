package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGColoredGlassBlock;
import com.supermartijn642.connectedglass.CGColoredPaneBlock;
import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.ConnectedGlass;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;

/**
 * Created 5/26/2020 by SuperMartijn642
 */
public class CGTagGenerator extends TagGenerator {

    @SuppressWarnings("unchecked")
    private static final Tag<Block>[] COLORED_GLASS_BLOCK_TAGS = new Tag[]{
        Tags.Blocks.GLASS_WHITE, Tags.Blocks.GLASS_ORANGE, Tags.Blocks.GLASS_MAGENTA, Tags.Blocks.GLASS_LIGHT_BLUE, Tags.Blocks.GLASS_YELLOW, Tags.Blocks.GLASS_LIME, Tags.Blocks.GLASS_PINK, Tags.Blocks.GLASS_GRAY, Tags.Blocks.GLASS_LIGHT_GRAY, Tags.Blocks.GLASS_CYAN, Tags.Blocks.GLASS_PURPLE, Tags.Blocks.GLASS_BLUE, Tags.Blocks.GLASS_BROWN, Tags.Blocks.GLASS_GREEN, Tags.Blocks.GLASS_RED, Tags.Blocks.GLASS_BLACK
    };
    @SuppressWarnings("unchecked")
    private static final Tag<Block>[] COLORED_PANE_BLOCK_TAGS = new Tag[]{
        Tags.Blocks.GLASS_PANES_WHITE, Tags.Blocks.GLASS_PANES_ORANGE, Tags.Blocks.GLASS_PANES_MAGENTA, Tags.Blocks.GLASS_PANES_LIGHT_BLUE, Tags.Blocks.GLASS_PANES_YELLOW, Tags.Blocks.GLASS_PANES_LIME, Tags.Blocks.GLASS_PANES_PINK, Tags.Blocks.GLASS_PANES_GRAY, Tags.Blocks.GLASS_PANES_LIGHT_GRAY, Tags.Blocks.GLASS_PANES_CYAN, Tags.Blocks.GLASS_PANES_PURPLE, Tags.Blocks.GLASS_PANES_BLUE, Tags.Blocks.GLASS_PANES_BROWN, Tags.Blocks.GLASS_PANES_GREEN, Tags.Blocks.GLASS_PANES_RED, Tags.Blocks.GLASS_PANES_BLACK
    };
    @SuppressWarnings("unchecked")
    private static final Tag<Item>[] COLORED_GLASS_ITEM_TAGS = new Tag[]{
        Tags.Items.GLASS_WHITE, Tags.Items.GLASS_ORANGE, Tags.Items.GLASS_MAGENTA, Tags.Items.GLASS_LIGHT_BLUE, Tags.Items.GLASS_YELLOW, Tags.Items.GLASS_LIME, Tags.Items.GLASS_PINK, Tags.Items.GLASS_GRAY, Tags.Items.GLASS_LIGHT_GRAY, Tags.Items.GLASS_CYAN, Tags.Items.GLASS_PURPLE, Tags.Items.GLASS_BLUE, Tags.Items.GLASS_BROWN, Tags.Items.GLASS_GREEN, Tags.Items.GLASS_RED, Tags.Items.GLASS_BLACK
    };
    @SuppressWarnings("unchecked")
    private static final Tag<Item>[] COLORED_PANE_ITEM_TAGS = new Tag[]{
        Tags.Items.GLASS_PANES_WHITE, Tags.Items.GLASS_PANES_ORANGE, Tags.Items.GLASS_PANES_MAGENTA, Tags.Items.GLASS_PANES_LIGHT_BLUE, Tags.Items.GLASS_PANES_YELLOW, Tags.Items.GLASS_PANES_LIME, Tags.Items.GLASS_PANES_PINK, Tags.Items.GLASS_PANES_GRAY, Tags.Items.GLASS_PANES_LIGHT_GRAY, Tags.Items.GLASS_PANES_CYAN, Tags.Items.GLASS_PANES_PURPLE, Tags.Items.GLASS_PANES_BLUE, Tags.Items.GLASS_PANES_BROWN, Tags.Items.GLASS_PANES_GREEN, Tags.Items.GLASS_PANES_RED, Tags.Items.GLASS_PANES_BLACK
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
        glass.add(ConnectedGlass.tinted_glass);
        glassTinted.add(ConnectedGlass.tinted_glass);

        glass.forEach(this.blockTag(Tags.Blocks.GLASS)::add);
        glass.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS)::add);
        glassColored.forEach(this.blockTag(Tags.Blocks.STAINED_GLASS)::add);
        glassColored.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.STAINED_GLASS)::add);
        glassColored.forEach(b -> {
            this.blockTag(COLORED_GLASS_BLOCK_TAGS[b.getColor().getId()]).add(b);
            this.itemTag(COLORED_GLASS_ITEM_TAGS[b.getColor().getId()]).add(b.asItem());
        });
        glassColorless.forEach(this.blockTag(Tags.Blocks.GLASS_COLORLESS)::add);
        glassColorless.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_COLORLESS)::add);
        glassTinted.forEach(this.blockTag("forge", "glass/tinted")::add);
        glassTinted.stream().map(Block::asItem).forEach(this.itemTag("forge", "glass/tinted")::add);
        panes.forEach(this.blockTag(Tags.Blocks.GLASS_PANES)::add);
        panes.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_PANES)::add);
        panesColored.forEach(this.blockTag(Tags.Blocks.STAINED_GLASS_PANES)::add);
        panesColored.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.STAINED_GLASS_PANES)::add);
        panesColored.forEach(b -> {
            this.blockTag(COLORED_PANE_BLOCK_TAGS[b.getColor().getId()]).add(b);
            this.itemTag(COLORED_PANE_ITEM_TAGS[b.getColor().getId()]).add(b.asItem());
        });
        panesColorless.forEach(this.blockTag(Tags.Blocks.GLASS_PANES_COLORLESS)::add);
        panesColorless.stream().map(Block::asItem).forEach(this.itemTag(Tags.Items.GLASS_PANES_COLORLESS)::add);
        panesTinted.forEach(this.blockTag("forge", "glass_panes/tinted")::add);
        panesTinted.stream().map(Block::asItem).forEach(this.itemTag("forge", "glass_panes/tinted")::add);

        // Impermeable tag
        TagBuilder<Block> impermeable = this.blockTag(BlockTags.IMPERMEABLE);
        for(CGGlassType type : CGGlassType.values())
            type.blocks.forEach(impermeable::add);
        impermeable.add(ConnectedGlass.tinted_glass);
    }
}
