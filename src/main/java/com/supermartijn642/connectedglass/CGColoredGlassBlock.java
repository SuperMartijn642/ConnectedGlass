package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredGlassBlock extends CGGlassBlock {

    private final EnumDyeColor color;

    public CGColoredGlassBlock(String texture, boolean connected, EnumDyeColor color){
        super(texture, connected);
        this.color = color;
    }

    public EnumDyeColor getColor(){
        return this.color;
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World level, BlockPos pos, BlockPos beaconPos){
        return this.color.getColorComponentValues();
    }
}
