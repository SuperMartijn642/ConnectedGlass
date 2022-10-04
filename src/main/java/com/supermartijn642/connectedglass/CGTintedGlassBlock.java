package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 7/23/2021 by SuperMartijn642
 */
public class CGTintedGlassBlock extends CGGlassBlock {

    public CGTintedGlassBlock(String texture, boolean connected){
        super(texture, connected);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess level, BlockPos pos){
        return 15;
    }
}
