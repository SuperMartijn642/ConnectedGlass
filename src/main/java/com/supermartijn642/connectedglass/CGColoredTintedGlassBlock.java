package com.supermartijn642.connectedglass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredTintedGlassBlock extends CGColoredGlassBlock {

    public CGColoredTintedGlassBlock(String texture, boolean connected, DyeColor color){
        super(texture, connected, color);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos){
        return false;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos){
        return level.getMaxLightLevel();
    }
}
