package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredTintedGlassBlock extends CGColoredGlassBlock {

    public CGColoredTintedGlassBlock(String registryName, String texture, boolean connected, EnumDyeColor color){
        super(registryName, texture, connected, color);
    }

    public CGColoredTintedGlassBlock(String registryName, boolean connected, EnumDyeColor color){
        super(registryName, registryName, connected, color);
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
