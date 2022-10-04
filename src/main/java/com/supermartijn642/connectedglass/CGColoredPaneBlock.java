package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredPaneBlock extends CGPaneBlock {

    public CGColoredPaneBlock(CGColoredGlassBlock block){
        super(block);
    }

    public EnumDyeColor getColor(){
        return ((CGColoredGlassBlock)this.block).getColor();
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos){
        return this.block.getBeaconColorMultiplier(state, world, pos, beaconPos);
    }
}
