package com.supermartijn642.connectedglass;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredTintedGlassBlock extends CGColoredGlassBlock {

    private DyeColor color;

    public CGColoredTintedGlassBlock(String registryName, String texture, boolean connected, DyeColor color){
        super(registryName, texture, connected, color);
    }

    public CGColoredTintedGlassBlock(String registryName, boolean connected, DyeColor color){
        super(registryName, registryName, connected, color);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos){
        return false;
    }

    @Override
    public int getLightBlock(BlockState state, IBlockReader world, BlockPos pos){
        return world.getMaxLightLevel();
    }
}
