package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 7/23/2021 by SuperMartijn642
 */
public class CGTintedGlassBlock extends CGGlassBlock {

    public CGTintedGlassBlock(String registryName, String texture, boolean connected){
        super(registryName, texture, connected);
    }

    public CGTintedGlassBlock(String registryName, boolean connected){
        super(registryName, connected);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos){
        return 15;
    }

    @Override
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.TRANSLUCENT;
    }
}
