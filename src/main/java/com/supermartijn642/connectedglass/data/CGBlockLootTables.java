package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.ConnectedGlass;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

/**
 * Created 6/24/2020 by SuperMartijn642
 */
public class CGBlockLootTables extends BlockLoot {

    @Override
    protected void addTables(){
        for(CGGlassType type : CGGlassType.values()){
            type.blocks.forEach(type.isTinted ? this::dropSelf : this::dropWhenSilkTouch);
            if(type.hasPanes)
                type.panes.forEach(type.isTinted ? this::dropSelf : this::dropWhenSilkTouch);
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
        ArrayList<Block> blocks = new ArrayList<>(ConnectedGlass.BLOCKS.size() + ConnectedGlass.PANES.size());
        blocks.addAll(ConnectedGlass.BLOCKS);
        blocks.addAll(ConnectedGlass.PANES);
        return blocks;
    }
}
