package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 6/23/2020 by SuperMartijn642
 */
public class CGTagProvider {

    public static Map<Tag<Block>,List<Block>> blockTags = new HashMap<>();
    public static Map<Tag<Item>,List<Block>> itemTags = new HashMap<>();

    public static void init(){
        ArrayList<Block> glass = new ArrayList<>();
        ArrayList<Block> glassColored = new ArrayList<>();
        ArrayList<Block> glassColorless = new ArrayList<>();
        ArrayList<Block> panes = new ArrayList<>();
        ArrayList<Block> panesColored = new ArrayList<>();
        ArrayList<Block> panesColorless = new ArrayList<>();

        for(CGGlassType type : CGGlassType.values()){
            glass.addAll(type.blocks);
            glassColored.addAll(type.colored_blocks.values());
            glassColorless.add(type.block);
            panes.addAll(type.panes);
            panesColored.addAll(type.colored_panes.values());
            panesColorless.add(type.pane);
        }

        add(glass, Tags.Blocks.GLASS, Tags.Items.GLASS);
        add(glassColored, Tags.Blocks.STAINED_GLASS, Tags.Items.STAINED_GLASS);
        add(glassColorless, Tags.Blocks.GLASS_COLORLESS, Tags.Items.GLASS_COLORLESS);
        add(panes, Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
        add(panesColored, Tags.Blocks.STAINED_GLASS_PANES, Tags.Items.STAINED_GLASS_PANES);
        add(panesColorless, Tags.Blocks.GLASS_PANES_COLORLESS, Tags.Items.GLASS_PANES_COLORLESS);
    }

    private static void add(List<Block> blocks, Tag<Block> blockTag, Tag<Item> itemTag){
        blockTags.put(blockTag,blocks);
        itemTags.put(itemTag,blocks);
    }

}
