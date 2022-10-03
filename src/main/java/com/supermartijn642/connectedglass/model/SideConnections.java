package com.supermartijn642.connectedglass.model;

import com.google.common.base.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class SideConnections {

    public final Direction side;
    private final BlockGetter level;
    private final Block block;

    public boolean left;
    public boolean right;
    public boolean up;
    public boolean up_left;
    public boolean up_right;
    public boolean down;
    public boolean down_left;
    public boolean down_right;

    public SideConnections(Direction side, BlockGetter level, BlockPos pos, Block block){
        this.side = side;
        this.level = level;
        this.block = level.getBlockState(pos).getBlock();

        Direction left;
        Direction right;
        Direction up;
        Direction down;
        if(side.getAxis() == Direction.Axis.Y){
            left = Direction.WEST;
            right = Direction.EAST;
            up = side == Direction.UP ? Direction.NORTH : Direction.SOUTH;
            down = side == Direction.UP ? Direction.SOUTH : Direction.NORTH;
        }else{
            left = side.getClockWise();
            right = side.getCounterClockWise();
            up = Direction.UP;
            down = Direction.DOWN;
        }

        this.left = this.isSameBlock(pos.relative(left));
        this.right = this.isSameBlock(pos.relative(right));
        this.up = this.isSameBlock(pos.relative(up));
        this.up_left = this.isSameBlock(pos.relative(up).relative(left));
        this.up_right = this.isSameBlock(pos.relative(up).relative(right));
        this.down = this.isSameBlock(pos.relative(down));
        this.down_left = this.isSameBlock(pos.relative(down).relative(left));
        this.down_right = this.isSameBlock(pos.relative(down).relative(right));
    }

    private boolean isSameBlock(BlockPos pos){
        return this.level.getBlockState(pos).getBlock() == this.block;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        SideConnections that = (SideConnections)o;
        return this.left == that.left && this.right == that.right && this.up == that.up && this.up_left == that.up_left && this.up_right == that.up_right && this.down == that.down && this.down_left == that.down_left && this.down_right == that.down_right && this.side == that.side;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.side, this.left, this.right, this.up, this.up_left, this.up_right, this.down, this.down_left, this.down_right);
    }
}
