package com.supermartijn642.connectedglass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

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
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos){
        return false;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos){
        return world.getMaxLightLevel();
    }
}
