package com.supermartijn642.connectedglass.model;

import com.supermartijn642.connectedglass.CGPaneBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGConnectedPaneBakedModel extends CGPaneBakedModel {

    public CGConnectedPaneBakedModel(CGPaneBlock block){
        super(block);
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData){
        ModelData modelData = new ModelData();
        for(Direction direction : Direction.Plane.HORIZONTAL){
            modelData.sides.put(direction, new SideData(direction, world, pos, state.getBlock()));
            BlockState upState = world.getBlockState(pos.above());
            boolean up = upState.getBlock() == state.getBlock() && upState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction));
            modelData.up.put(direction, up);
            modelData.upPost = upState.getBlock() == state.getBlock();
            BlockState downState = world.getBlockState(pos.below());
            boolean down = downState.getBlock() == state.getBlock() && downState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction));
            modelData.down.put(direction, down);
            modelData.downPost = downState.getBlock() == state.getBlock();
        }

        return modelData;
    }

    @Override
    protected boolean isEnabledUp(Direction part, IModelData extraData){
        return extraData instanceof ModelData && (part == null ? ((ModelData)extraData).upPost : ((ModelData)extraData).up.get(part));
    }

    @Override
    protected boolean isEnabledDown(Direction part, IModelData extraData){
        return extraData instanceof ModelData && (part == null ? ((ModelData)extraData).downPost : ((ModelData)extraData).down.get(part));
    }

    @Override
    protected float[] getBorderUV(){
        return this.getUV(0, 7);
    }

    @Override
    protected float[] getUV(Direction side, IModelData modelData){
        if(side == Direction.UP || side == Direction.DOWN)
            return this.getBorderUV();

        if(!(modelData instanceof ModelData))
            return getUV(0, 0);

        SideData blocks = ((ModelData)modelData).sides.get(side);
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

    private static class ModelData implements IModelData {

        public Map<Direction,SideData> sides = new HashMap<>();
        public Map<Direction,Boolean> up = new HashMap<>(), down = new HashMap<>();
        public boolean upPost, downPost;

        @Override
        public boolean hasProperty(ModelProperty<?> prop){
            return false;
        }

        @Nullable
        @Override
        public <T> T getData(ModelProperty<T> prop){
            return null;
        }

        @Nullable
        @Override
        public <T> T setData(ModelProperty<T> prop, T data){
            return null;
        }
    }

    private static class SideData {

        private IBlockReader world;
        private Block block;

        public boolean left;
        public boolean right;
        public boolean up;
        public boolean up_left;
        public boolean up_right;
        public boolean down;
        public boolean down_left;
        public boolean down_right;

        public SideData(Direction side, IBlockReader world, BlockPos pos, Block block){
            this.world = world;
            this.block = block;

            Direction left;
            Direction right;
            Direction up;
            Direction down;
            if(side.getAxis() == Direction.Axis.Y){
                left = side == Direction.UP ? Direction.WEST : Direction.EAST;
                right = side == Direction.UP ? Direction.EAST : Direction.WEST;
                up = Direction.NORTH;
                down = Direction.SOUTH;
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
            return this.world.getBlockState(pos).getBlock() == this.block;
        }
    }

}
