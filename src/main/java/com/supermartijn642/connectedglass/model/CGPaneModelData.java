package com.supermartijn642.connectedglass.model;

import net.minecraft.block.BlockPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class CGPaneModelData extends CGModelData {

    public static CGPaneModelData create(IBlockAccess level, BlockPos pos, IBlockState state){
        return new CGPaneModelData(
            level.getBlockState(pos.up()).getActualState(level, pos.up()),
            level.getBlockState(pos.down()).getActualState(level, pos.down()),
            new SideConnections(EnumFacing.NORTH, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.EAST, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.SOUTH, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.WEST, level, pos, state.getBlock())
        );
    }

    private final IBlockState stateAbove, stateBelow;

    protected CGPaneModelData(IBlockState stateAbove, IBlockState stateBelow, SideConnections north, SideConnections east, SideConnections south, SideConnections west){
        super(null, null, north, east, south, west);
        this.stateAbove = stateAbove;
        this.stateBelow = stateBelow;
        this.hashCode += stateAbove.hashCode() + stateBelow.hashCode();
    }

    public boolean isAbovePane(){
        return this.getSideData(EnumFacing.NORTH).up;
    }

    public boolean isBelowPane(){
        return this.getSideData(EnumFacing.NORTH).down;
    }

    public boolean isAboveConnectedTo(EnumFacing side){
        if(!this.getSideData(EnumFacing.NORTH).up)
            return false;

        switch(side){
            case NORTH:
                return this.stateAbove.getValue(BlockPane.NORTH);
            case SOUTH:
                return this.stateAbove.getValue(BlockPane.SOUTH);
            case WEST:
                return this.stateAbove.getValue(BlockPane.WEST);
            case EAST:
                return this.stateAbove.getValue(BlockPane.EAST);
        }
        return false;
    }

    public boolean isBelowConnectedTo(EnumFacing side){
        if(!this.getSideData(EnumFacing.NORTH).down)
            return false;

        switch(side){
            case NORTH:
                return this.stateBelow.getValue(BlockPane.NORTH);
            case SOUTH:
                return this.stateBelow.getValue(BlockPane.SOUTH);
            case WEST:
                return this.stateBelow.getValue(BlockPane.WEST);
            case EAST:
                return this.stateBelow.getValue(BlockPane.EAST);
        }
        return false;
    }
}
