package com.supermartijn642.connectedglass.model;

import com.google.common.base.Objects;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class SideConnections {

    public final EnumFacing side;
    private final IBlockAccess level;
    private final Block block;

    public boolean left;
    public boolean right;
    public boolean up;
    public boolean up_left;
    public boolean up_right;
    public boolean down;
    public boolean down_left;
    public boolean down_right;

    public SideConnections(EnumFacing side, IBlockAccess level, BlockPos pos, Block block){
        this.side = side;
        this.level = level;
        this.block = level.getBlockState(pos).getBlock();

        EnumFacing left;
        EnumFacing right;
        EnumFacing up;
        EnumFacing down;
        if(side.getAxis() == EnumFacing.Axis.Y){
            left = EnumFacing.WEST;
            right = EnumFacing.EAST;
            up = side == EnumFacing.UP ? EnumFacing.NORTH : EnumFacing.SOUTH;
            down = side == EnumFacing.UP ? EnumFacing.SOUTH : EnumFacing.NORTH;
        }else{
            left = side.rotateY();
            right = side.rotateYCCW();
            up = EnumFacing.UP;
            down = EnumFacing.DOWN;
        }

        this.left = this.isSameBlock(pos.offset(left));
        this.right = this.isSameBlock(pos.offset(right));
        this.up = this.isSameBlock(pos.offset(up));
        this.up_left = this.isSameBlock(pos.offset(up).offset(left));
        this.up_right = this.isSameBlock(pos.offset(up).offset(right));
        this.down = this.isSameBlock(pos.offset(down));
        this.down_left = this.isSameBlock(pos.offset(down).offset(left));
        this.down_right = this.isSameBlock(pos.offset(down).offset(right));
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
