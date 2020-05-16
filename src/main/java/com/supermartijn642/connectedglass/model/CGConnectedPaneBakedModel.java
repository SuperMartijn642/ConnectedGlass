package com.supermartijn642.connectedglass.model;

import com.supermartijn642.connectedglass.CGPaneBlock;
import net.minecraft.block.BlockPane;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGConnectedPaneBakedModel extends CGPaneBakedModel {

    public CGConnectedPaneBakedModel(CGPaneBlock block){
        super(block);
    }

    public static CGPaneModelData getModelData(IBlockAccess world, BlockPos pos, IBlockState state){
        CGPaneModelData modelData = new CGPaneModelData();

        for(EnumFacing direction : EnumFacing.Plane.HORIZONTAL){
            modelData.sides.put(direction, new CGSideData(direction, world, pos, state.getBlock()));
            IBlockState upState = world.getBlockState(pos.up()).getActualState(world, pos.up());
            IProperty<Boolean> property = direction == EnumFacing.NORTH ? BlockPane.NORTH : direction == EnumFacing.EAST ? BlockPane.EAST : direction == EnumFacing.SOUTH ? BlockPane.SOUTH : direction == EnumFacing.WEST ? BlockPane.WEST : null;
            boolean up = upState.getBlock() == state.getBlock() && upState.getValue(property);
            modelData.up.put(direction, up);
            modelData.upPost = upState.getBlock() == state.getBlock();
            IBlockState downState = world.getBlockState(pos.down()).getActualState(world, pos.down());
            boolean down = downState.getBlock() == state.getBlock() && downState.getValue(property);
            modelData.down.put(direction, down);
            modelData.downPost = downState.getBlock() == state.getBlock();
        }

        return modelData;
    }

    @Override
    protected boolean isEnabledUp(EnumFacing part, CGPaneModelData extraData){
        return extraData != null && (part == null ? extraData.upPost : extraData.up.get(part));
    }

    @Override
    protected boolean isEnabledDown(EnumFacing part, CGPaneModelData extraData){
        return extraData != null && (part == null ? extraData.downPost : extraData.down.get(part));
    }

    @Override
    protected float[] getBorderUV(){
        return this.getUV(0, 7);
    }

    @Override
    protected float[] getUV(EnumFacing side, CGPaneModelData modelData){
        if(side == EnumFacing.UP || side == EnumFacing.DOWN)
            return this.getBorderUV();

        if(modelData == null)
            return getUV(0, 0);

        CGSideData blocks = modelData.sides.get(side);
        float[] uv;

        if(!blocks.left && !blocks.up && !blocks.right && !blocks.down) // all directions
            uv = this.getUV(0, 0);
        else{ // one direction
            if(blocks.left && !blocks.up && !blocks.right && !blocks.down)
                uv = this.getUV(3, 0);
            else if(!blocks.left && blocks.up && !blocks.right && !blocks.down)
                uv = this.getUV(0, 3);
            else if(!blocks.left && !blocks.up && blocks.right && !blocks.down)
                uv = this.getUV(1, 0);
            else if(!blocks.left && !blocks.up && !blocks.right && blocks.down)
                uv = this.getUV(0, 1);
            else{ // two directions
                if(blocks.left && !blocks.up && blocks.right && !blocks.down)
                    uv = this.getUV(2, 0);
                else if(!blocks.left && blocks.up && !blocks.right && blocks.down)
                    uv = this.getUV(0, 2);
                else if(blocks.left && blocks.up && !blocks.right && !blocks.down){
                    if(blocks.up_left)
                        uv = this.getUV(3, 3);
                    else
                        uv = this.getUV(5, 1);
                }else if(!blocks.left && blocks.up && blocks.right && !blocks.down){
                    if(blocks.up_right)
                        uv = this.getUV(1, 3);
                    else
                        uv = this.getUV(4, 1);
                }else if(!blocks.left && !blocks.up && blocks.right && blocks.down){
                    if(blocks.down_right)
                        uv = this.getUV(1, 1);
                    else
                        uv = this.getUV(4, 0);
                }else if(blocks.left && !blocks.up && !blocks.right && blocks.down){
                    if(blocks.down_left)
                        uv = this.getUV(3, 1);
                    else
                        uv = this.getUV(5, 0);
                }else{ // three directions
                    if(!blocks.left){
                        if(blocks.up_right && blocks.down_right)
                            uv = this.getUV(1, 2);
                        else if(blocks.up_right)
                            uv = this.getUV(4, 2);
                        else if(blocks.down_right)
                            uv = this.getUV(6, 2);
                        else
                            uv = this.getUV(6, 0);
                    }else if(!blocks.up){
                        if(blocks.down_left && blocks.down_right)
                            uv = this.getUV(2, 1);
                        else if(blocks.down_left)
                            uv = this.getUV(7, 2);
                        else if(blocks.down_right)
                            uv = this.getUV(5, 2);
                        else
                            uv = this.getUV(7, 0);
                    }else if(!blocks.right){
                        if(blocks.up_left && blocks.down_left)
                            uv = this.getUV(3, 2);
                        else if(blocks.up_left)
                            uv = this.getUV(7, 3);
                        else if(blocks.down_left)
                            uv = this.getUV(5, 3);
                        else
                            uv = this.getUV(7, 1);
                    }else if(!blocks.down){
                        if(blocks.up_left && blocks.up_right)
                            uv = this.getUV(2, 3);
                        else if(blocks.up_left)
                            uv = this.getUV(4, 3);
                        else if(blocks.up_right)
                            uv = this.getUV(6, 3);
                        else
                            uv = this.getUV(6, 1);
                    }else{ // four directions
                        if(blocks.up_left && blocks.up_right && blocks.down_left && blocks.down_right)
                            uv = this.getUV(2, 2);
                        else{
                            if(!blocks.up_left && blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = this.getUV(7, 7);
                            else if(blocks.up_left && !blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = this.getUV(6, 7);
                            else if(blocks.up_left && blocks.up_right && !blocks.down_left && blocks.down_right)
                                uv = this.getUV(7, 6);
                            else if(blocks.up_left && blocks.up_right && blocks.down_left && !blocks.down_right)
                                uv = this.getUV(6, 6);
                            else{
                                if(!blocks.up_left && blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = this.getUV(0, 4);
                                else if(blocks.up_left && !blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = this.getUV(0, 5);
                                else if(!blocks.up_left && !blocks.up_right && blocks.down_right && blocks.down_left)
                                    uv = this.getUV(3, 6);
                                else if(blocks.up_left && !blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = this.getUV(3, 7);
                                else if(blocks.up_left && blocks.up_right && !blocks.down_right && !blocks.down_left)
                                    uv = this.getUV(2, 7);
                                else if(!blocks.up_left && blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = this.getUV(2, 6);
                                else{
                                    if(blocks.up_left)
                                        uv = this.getUV(5, 7);
                                    else if(blocks.up_right)
                                        uv = this.getUV(4, 7);
                                    else if(blocks.down_right)
                                        uv = this.getUV(4, 6);
                                    else if(blocks.down_left)
                                        uv = this.getUV(5, 6);
                                    else
                                        uv = this.getUV(0, 6);
                                }
                            }
                        }
                    }
                }
            }
        }

        return uv;
    }

    private float[] getUV(int x, int y){
        return new float[]{x * 2, y * 2, (x + 1) * 2, (y + 1) * 2};
    }

}
