package com.supermartijn642.connectedglass;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredTintedGlassBlock extends CGColoredGlassBlock {

    public CGColoredTintedGlassBlock(String texture, boolean connected, DyeColor color){
        super(texture, connected, color);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader level, BlockPos pos){
        return false;
    }

    @Override
    public int getLightBlock(BlockState state, IBlockReader level, BlockPos pos){
        return level.getMaxLightLevel();
    }
}
