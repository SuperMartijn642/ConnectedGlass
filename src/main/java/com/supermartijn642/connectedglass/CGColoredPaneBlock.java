package com.supermartijn642.connectedglass;

import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.item.DyeColor;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredPaneBlock extends CGPaneBlock implements IBeaconBeamColorProvider {

    public CGColoredPaneBlock(CGColoredGlassBlock block){
        super(block);
    }

    @Override
    public DyeColor getColor(){
        return ((CGColoredGlassBlock)this.block).getColor();
    }
}
