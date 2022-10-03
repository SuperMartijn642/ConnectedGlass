package com.supermartijn642.connectedglass.model;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class CGPaneModelData extends CGModelData {

    public static CGPaneModelData create(BlockGetter level, BlockPos pos, BlockState state){
        return new CGPaneModelData(
            level.getBlockState(pos.above()),
            level.getBlockState(pos.below()),
            new SideConnections(Direction.NORTH, level, pos, state.getBlock()),
            new SideConnections(Direction.EAST, level, pos, state.getBlock()),
            new SideConnections(Direction.SOUTH, level, pos, state.getBlock()),
            new SideConnections(Direction.WEST, level, pos, state.getBlock())
        );
    }

    private final BlockState stateAbove, stateBelow;

    protected CGPaneModelData(BlockState stateAbove, BlockState stateBelow, SideConnections north, SideConnections east, SideConnections south, SideConnections west){
        super(null, null, north, east, south, west);
        this.stateAbove = stateAbove;
        this.stateBelow = stateBelow;
        this.hashCode += stateAbove.hashCode() + stateBelow.hashCode();
    }

    public boolean isAbovePane(){
        return this.getSideData(Direction.NORTH).up;
    }

    public boolean isBelowPane(){
        return this.getSideData(Direction.NORTH).down;
    }

    public boolean isAboveConnectedTo(Direction side){
        if(!this.getSideData(Direction.NORTH).up)
            return false;

        switch(side){
            case NORTH:
                return this.stateAbove.getValue(BlockStateProperties.NORTH);
            case SOUTH:
                return this.stateAbove.getValue(BlockStateProperties.SOUTH);
            case WEST:
                return this.stateAbove.getValue(BlockStateProperties.WEST);
            case EAST:
                return this.stateAbove.getValue(BlockStateProperties.EAST);
        }
        return false;
    }

    public boolean isBelowConnectedTo(Direction side){
        if(!this.getSideData(Direction.NORTH).down)
            return false;

        switch(side){
            case NORTH:
                return this.stateBelow.getValue(BlockStateProperties.NORTH);
            case SOUTH:
                return this.stateBelow.getValue(BlockStateProperties.SOUTH);
            case WEST:
                return this.stateBelow.getValue(BlockStateProperties.WEST);
            case EAST:
                return this.stateBelow.getValue(BlockStateProperties.EAST);
        }
        return false;
    }
}
