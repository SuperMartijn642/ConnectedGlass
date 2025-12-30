package com.supermartijn642.connectedglass;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredTintedGlassBlock extends CGColoredGlassBlock {

    public CGColoredTintedGlassBlock(Identifier identifier, String texture, boolean connected, DyeColor color){
        super(identifier, texture, connected, color);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState blockState){
        return false;
    }

    @Override
    protected int getLightBlock(BlockState blockState){
        return 15;
    }
}
