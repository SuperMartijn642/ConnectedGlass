package com.supermartijn642.connectedglass.model;

import com.mojang.math.Vector3f;
import com.supermartijn642.connectedglass.CGPaneBlock;
import com.supermartijn642.connectedglass.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBakedModel implements IDynamicBakedModel {

    private static final FaceBakery BAKERY = new FaceBakery();

    private final CGPaneBlock pane;

    public CGPaneBakedModel(CGPaneBlock pane){
        this.pane = pane;
    }

    @Override
    public boolean useAmbientOcclusion(){
        return false;
    }

    @Override
    public boolean isGui3d(){
        return false;
    }

    @Override
    public boolean usesBlockLight(){
        return true;
    }

    @Override
    public boolean isCustomRenderer(){
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(){
        return this.getTexture();
    }

    @Override
    public ItemOverrides getOverrides(){
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms(){
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(ForgeRegistries.BLOCKS.getKey(Blocks.STONE), "")).getTransforms();
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData){
        List<BakedQuad> quads = new ArrayList<>();

        Direction[] sides = side == null ? Direction.values() : new Direction[]{side};
        boolean culling = side != null;
        for(Direction side2 : sides){
            quads.addAll(this.getPostQuad(side2, culling, this.isEnabledUp(null, extraData), this.isEnabledDown(null, extraData)));

            float[] uv = this.getUV(side2, extraData);
            for(Direction part : Direction.Plane.HORIZONTAL)
                quads.addAll(this.getPartQuad(state, part, side2, uv, culling, this.isEnabledUp(part, extraData), this.isEnabledDown(part, extraData)));
        }

        return quads;
    }

    protected TextureAtlasSprite getTexture(){
        return ClientProxy.TEXTURES.get(this.pane.block);
    }

    protected float[] getUV(Direction side, IModelData modelData){
        if(side == Direction.UP || side == Direction.DOWN)
            return this.getBorderUV();
        return new float[]{0, 0, 16, 16};
    }

    protected List<BakedQuad> getPostQuad(Direction side, boolean culling, boolean isEnabledUp, boolean isEnabledDown){
        if(side.getAxis() != Direction.Axis.Y)
            return Collections.emptyList();

        boolean hasCulling = side == Direction.UP ? isEnabledUp : isEnabledDown;
        if(hasCulling != culling)
            return Collections.emptyList();

        Vector3f from = new Vector3f(7, 0, 7), to = new Vector3f(9, 16, 9);
        float[] uv = new float[]{7 / 8f, 2 * 7 + 7 / 8f, 9 / 8f, 2 * 7 + 9 / 8f};
        BlockElementFace face = new BlockElementFace(hasCulling ? side : null, -1, "", new BlockFaceUV(uv, 0));

        BakedQuad quad = BAKERY.bakeQuad(from, to, face, getTexture(), side, BlockModelRotation.X0_Y0, null, true, null);
        return Collections.singletonList(quad);
    }

    protected List<BakedQuad> getPartQuad(BlockState state, Direction part, Direction side, float[] totalUV, boolean culling, boolean isEnabledUp, boolean isEnabledDown){
        List<BakedQuad> quads = new ArrayList<>();
        float unitW = (totalUV[2] - totalUV[0]) / 16, unitH = (totalUV[3] - totalUV[1]) / 16; // width and height of one pixel

        BooleanProperty property = part == Direction.NORTH ? CrossCollisionBlock.NORTH : part == Direction.EAST ? CrossCollisionBlock.EAST : part == Direction.SOUTH ? CrossCollisionBlock.SOUTH : part == Direction.WEST ? CrossCollisionBlock.WEST : null;
        if((state == null && (part == Direction.NORTH || part == Direction.SOUTH)) || (state != null && state.getValue(property))){

            boolean hasQuad = true;
            float[] uv = new float[0];
            boolean hasCulling = false;
            int rotation = 0;

            if(side == Direction.UP || side == Direction.DOWN){
                hasCulling = side == Direction.UP ? isEnabledUp : isEnabledDown;
                if(part == Direction.NORTH || part == Direction.EAST)
                    uv = new float[]{totalUV[0] + 7 * unitW, totalUV[1], totalUV[0] + 9 * unitW, totalUV[1] + 7 * unitH};
                else
                    uv = new float[]{totalUV[0] + 7 * unitW, totalUV[1] + 9 * unitH, totalUV[0] + 9 * unitW, totalUV[3]};
                if(part.getAxis() == Direction.Axis.X)
                    rotation = 90;
            }else if(side.getOpposite() == part)
                hasQuad = false;
            else if(side == part){
                uv = this.getBorderUV();
                uv[0] += 7 * unitW;
                uv[2] -= 7 * unitW;
                hasCulling = true;
            }else if(side.getCounterClockWise() == part)
                uv = new float[]{totalUV[0] + 9 * unitW, totalUV[1], totalUV[2], totalUV[3]};
            else if(side.getClockWise() == part)
                uv = new float[]{totalUV[0], totalUV[1], totalUV[0] + 7 * unitW, totalUV[3]};

            if(hasQuad && hasCulling == culling){
                Vector3f from = getPartFromPos(part), to = getPartToPos(part);
                BlockElementFace face = new BlockElementFace(hasCulling ? side : null, -1, "", new BlockFaceUV(uv, rotation));
                quads.add(BAKERY.bakeQuad(from, to, face, getTexture(), side, BlockModelRotation.X0_Y0, null, true, null));
            }
        }else if(side == part && !culling){
            Vector3f from = new Vector3f(7, 0, 7), to = new Vector3f(9, 16, 9);
            float[] uv = new float[]{totalUV[0] + 7 * unitW, totalUV[1], totalUV[0] + 9 * unitH, totalUV[3]};
            BlockElementFace face = new BlockElementFace(null, -1, "", new BlockFaceUV(uv, 0));
            quads.add(BAKERY.bakeQuad(from, to, face, getTexture(), side, BlockModelRotation.X0_Y0, null, true, null));
        }

        return quads;
    }

    private Vector3f getPartFromPos(Direction part){
        if(part == Direction.NORTH)
            return new Vector3f(7, 0, 0);
        else if(part == Direction.EAST)
            return new Vector3f(9, 0, 7);
        else if(part == Direction.SOUTH)
            return new Vector3f(7, 0, 9);
        else if(part == Direction.WEST)
            return new Vector3f(0, 0, 7);
        return null;
    }

    private Vector3f getPartToPos(Direction part){
        if(part == Direction.NORTH)
            return new Vector3f(9, 16, 7);
        else if(part == Direction.EAST)
            return new Vector3f(16, 16, 9);
        else if(part == Direction.SOUTH)
            return new Vector3f(9, 16, 16);
        else if(part == Direction.WEST)
            return new Vector3f(7, 16, 9);
        return null;
    }

    protected float[] getBorderUV(){
        return new float[]{0, 0, 16, 16};
    }

    protected boolean isEnabledUp(Direction part, IModelData extraData){
        return false;
    }

    protected boolean isEnabledDown(Direction part, IModelData extraData){
        return false;
    }

}
