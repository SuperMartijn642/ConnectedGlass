package com.supermartijn642.connectedglass.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGConnectedBakedModel extends CGBakedModel {

    public CGConnectedBakedModel(IBakedModel original){
        super(original);
    }

    @Override
    public @Nonnull IModelData getModelData(@Nonnull ILightReader level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData){
        return new ModelDataMap.Builder().withInitial(CGModelData.MODEL_PROPERTY, CGModelData.create(level, pos, state)).build();
    }

    @Override
    protected BakedQuad remapQuad(BakedQuad quad, CGModelData modelData){
        int[] vertexData = quad.getVertices();
        // Make sure we don't change the original quad
        vertexData = Arrays.copyOf(vertexData, vertexData.length);

        // Adjust the uv
        int[] newUV = this.getUV(modelData == null ? null : modelData.getSideData(quad.getDirection()));
        adjustVertexDataUV(vertexData, newUV[0], newUV[1], quad.getSprite(), DefaultVertexFormats.BLOCK);

        // Create a new quad
        return new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.shouldApplyDiffuseLighting());
    }

    protected int[] getUV(SideConnections sideData){
        if(sideData == null)
            return new int[]{0, 0};

        int[] uv;

        if(!sideData.left && !sideData.up && !sideData.right && !sideData.down) // all directions
            uv = new int[]{0, 0};
        else{ // one direction
            if(sideData.left && !sideData.up && !sideData.right && !sideData.down)
                uv = new int[]{3, 0};
            else if(!sideData.left && sideData.up && !sideData.right && !sideData.down)
                uv = new int[]{0, 3};
            else if(!sideData.left && !sideData.up && sideData.right && !sideData.down)
                uv = new int[]{1, 0};
            else if(!sideData.left && !sideData.up && !sideData.right && sideData.down)
                uv = new int[]{0, 1};
            else{ // two directions
                if(sideData.left && !sideData.up && sideData.right && !sideData.down)
                    uv = new int[]{2, 0};
                else if(!sideData.left && sideData.up && !sideData.right && sideData.down)
                    uv = new int[]{0, 2};
                else if(sideData.left && sideData.up && !sideData.right && !sideData.down){
                    if(sideData.up_left)
                        uv = new int[]{3, 3};
                    else
                        uv = new int[]{5, 1};
                }else if(!sideData.left && sideData.up && sideData.right && !sideData.down){
                    if(sideData.up_right)
                        uv = new int[]{1, 3};
                    else
                        uv = new int[]{4, 1};
                }else if(!sideData.left && !sideData.up && sideData.right && sideData.down){
                    if(sideData.down_right)
                        uv = new int[]{1, 1};
                    else
                        uv = new int[]{4, 0};
                }else if(sideData.left && !sideData.up && !sideData.right && sideData.down){
                    if(sideData.down_left)
                        uv = new int[]{3, 1};
                    else
                        uv = new int[]{5, 0};
                }else{ // three directions
                    if(!sideData.left){
                        if(sideData.up_right && sideData.down_right)
                            uv = new int[]{1, 2};
                        else if(sideData.up_right)
                            uv = new int[]{4, 2};
                        else if(sideData.down_right)
                            uv = new int[]{6, 2};
                        else
                            uv = new int[]{6, 0};
                    }else if(!sideData.up){
                        if(sideData.down_left && sideData.down_right)
                            uv = new int[]{2, 1};
                        else if(sideData.down_left)
                            uv = new int[]{7, 2};
                        else if(sideData.down_right)
                            uv = new int[]{5, 2};
                        else
                            uv = new int[]{7, 0};
                    }else if(!sideData.right){
                        if(sideData.up_left && sideData.down_left)
                            uv = new int[]{3, 2};
                        else if(sideData.up_left)
                            uv = new int[]{7, 3};
                        else if(sideData.down_left)
                            uv = new int[]{5, 3};
                        else
                            uv = new int[]{7, 1};
                    }else if(!sideData.down){
                        if(sideData.up_left && sideData.up_right)
                            uv = new int[]{2, 3};
                        else if(sideData.up_left)
                            uv = new int[]{4, 3};
                        else if(sideData.up_right)
                            uv = new int[]{6, 3};
                        else
                            uv = new int[]{6, 1};
                    }else{ // four directions
                        if(sideData.up_left && sideData.up_right && sideData.down_left && sideData.down_right)
                            uv = new int[]{2, 2};
                        else{
                            if(!sideData.up_left && sideData.up_right && sideData.down_left && sideData.down_right)
                                uv = new int[]{7, 7};
                            else if(sideData.up_left && !sideData.up_right && sideData.down_left && sideData.down_right)
                                uv = new int[]{6, 7};
                            else if(sideData.up_left && sideData.up_right && !sideData.down_left && sideData.down_right)
                                uv = new int[]{7, 6};
                            else if(sideData.up_left && sideData.up_right && sideData.down_left && !sideData.down_right)
                                uv = new int[]{6, 6};
                            else{
                                if(!sideData.up_left && sideData.up_right && !sideData.down_right && sideData.down_left)
                                    uv = new int[]{0, 4};
                                else if(sideData.up_left && !sideData.up_right && sideData.down_right && !sideData.down_left)
                                    uv = new int[]{0, 5};
                                else if(!sideData.up_left && !sideData.up_right && sideData.down_right && sideData.down_left)
                                    uv = new int[]{3, 6};
                                else if(sideData.up_left && !sideData.up_right && !sideData.down_right && sideData.down_left)
                                    uv = new int[]{3, 7};
                                else if(sideData.up_left && sideData.up_right && !sideData.down_right && !sideData.down_left)
                                    uv = new int[]{2, 7};
                                else if(!sideData.up_left && sideData.up_right && sideData.down_right && !sideData.down_left)
                                    uv = new int[]{2, 6};
                                else{
                                    if(sideData.up_left)
                                        uv = new int[]{5, 7};
                                    else if(sideData.up_right)
                                        uv = new int[]{4, 7};
                                    else if(sideData.down_right)
                                        uv = new int[]{4, 6};
                                    else if(sideData.down_left)
                                        uv = new int[]{5, 6};
                                    else
                                        uv = new int[]{0, 6};
                                }
                            }
                        }
                    }
                }
            }
        }

        return uv;
    }
}
