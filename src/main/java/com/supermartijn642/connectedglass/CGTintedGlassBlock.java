package com.supermartijn642.connectedglass;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 7/23/2021 by SuperMartijn642
 */
public class CGTintedGlassBlock extends CGGlassBlock {

    public CGTintedGlassBlock(String texture, boolean connected){
        super(texture, connected);
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
