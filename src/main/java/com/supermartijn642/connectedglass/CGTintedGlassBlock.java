package com.supermartijn642.connectedglass;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created 7/23/2021 by SuperMartijn642
 */
public class CGTintedGlassBlock extends CGGlassBlock {

    public CGTintedGlassBlock(Identifier identifier, String texture, boolean connected){
        super(identifier, texture, connected);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState blockState){
        return false;
    }

    @Override
    protected int getLightDampening(BlockState blockState){
        return 15;
    }
}
