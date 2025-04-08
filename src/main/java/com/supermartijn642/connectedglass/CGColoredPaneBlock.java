package com.supermartijn642.connectedglass;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredPaneBlock extends CGPaneBlock implements BeaconBeamBlock {

    public CGColoredPaneBlock(ResourceLocation identifier, CGColoredGlassBlock block){
        super(identifier, block);
    }

    @Override
    public DyeColor getColor(){
        return ((CGColoredGlassBlock)this.block).getColor();
    }
}
