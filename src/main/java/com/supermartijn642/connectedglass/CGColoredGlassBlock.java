package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredGlassBlock extends CGGlassBlock {

    public final EnumDyeColor color;

    public CGColoredGlassBlock(String registryName, String texture, boolean connected, EnumDyeColor color){
        super(registryName, texture, connected);
        this.color = color;
    }

    public CGColoredGlassBlock(String registryName, boolean connected, EnumDyeColor color){
        this(registryName, registryName, connected, color);
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos){
        return this.color.getColorComponentValues();
    }

    @Override
    public CGColoredPaneBlock createPane(){
        return new CGColoredPaneBlock(this);
    }

    @Override
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.TRANSLUCENT;
    }
}
