package com.supermartijn642.connectedglass.model;

import com.supermartijn642.connectedglass.CGPaneBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGConnectedPaneBakedModel extends CGPaneBakedModel {

    private static final ModelProperty<PaneModelData> PANE_MODEL_DATA_PROPERTY = new ModelProperty<>();

    public CGConnectedPaneBakedModel(CGPaneBlock block){
        super(block);
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData){
        PaneModelData modelData = new PaneModelData();
        for(Direction direction : Direction.Plane.HORIZONTAL){
            modelData.sides.put(direction, new SideData(direction, world, pos, state.getBlock()));
            BlockState upState = world.getBlockState(pos.above());
            boolean up = upState.getBlock() == state.getBlock() && upState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
            modelData.up.put(direction, up);
            modelData.upPost = upState.getBlock() == state.getBlock();
            BlockState downState = world.getBlockState(pos.below());
            boolean down = downState.getBlock() == state.getBlock() && downState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
            modelData.down.put(direction, down);
            modelData.downPost = downState.getBlock() == state.getBlock();
        }

        return ModelData.builder().with(PANE_MODEL_DATA_PROPERTY, modelData).build();
    }

    @Override
    protected boolean isEnabledUp(Direction part, ModelData extraData){
        return extraData.has(PANE_MODEL_DATA_PROPERTY) && (part == null ? extraData.get(PANE_MODEL_DATA_PROPERTY).upPost : extraData.get(PANE_MODEL_DATA_PROPERTY).up.get(part));
    }

    @Override
    protected boolean isEnabledDown(Direction part, ModelData extraData){
        return extraData.has(PANE_MODEL_DATA_PROPERTY) && (part == null ? extraData.get(PANE_MODEL_DATA_PROPERTY).downPost : extraData.get(PANE_MODEL_DATA_PROPERTY).down.get(part));
    }

    @Override
    protected float[] getBorderUV(){
        return this.getUV(0, 7);
    }

    @Override
    protected float[] getUV(Direction side, ModelData modelData){
        if(side == Direction.UP || side == Direction.DOWN)
            return this.getBorderUV();

        if(!modelData.has(PANE_MODEL_DATA_PROPERTY))
            return this.getUV(0, 0);

        SideData blocks = modelData.get(PANE_MODEL_DATA_PROPERTY).sides.get(side);
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

    private static class PaneModelData {

        public Map<Direction,SideData> sides = new HashMap<>();
        public Map<Direction,Boolean> up = new HashMap<>(), down = new HashMap<>();
        public boolean upPost, downPost;
    }

    private static class SideData {

        private BlockGetter world;
        private Block block;

        public boolean left;
        public boolean right;
        public boolean up;
        public boolean up_left;
        public boolean up_right;
        public boolean down;
        public boolean down_left;
        public boolean down_right;

        public SideData(Direction side, BlockGetter world, BlockPos pos, Block block){
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
