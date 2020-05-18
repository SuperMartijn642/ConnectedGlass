package com.supermartijn642.connectedglass.model;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 5/13/2020 by SuperMartijn642
 */
public class CGSideData {

    private IBlockAccess world;
    private Block block;

    public boolean left;
    public boolean right;
    public boolean up;
    public boolean up_left;
    public boolean up_right;
    public boolean down;
    public boolean down_left;
    public boolean down_right;

    public CGSideData(EnumFacing side, IBlockAccess world, BlockPos pos, Block block){
        this.world = world;
        this.block = block;

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
        return this.world.getBlockState(pos).getBlock() == this.block;
    }

}
