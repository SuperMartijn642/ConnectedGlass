package com.supermartijn642.connectedglass.model;

import com.supermartijn642.connectedglass.CGPaneBlock;
import com.supermartijn642.connectedglass.ClientProxy;
import net.minecraft.block.BlockPane;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBakedModel implements IBakedModel {

    private static final FaceBakery BAKERY = new FaceBakery();

    private final CGPaneBlock pane;

    public CGPaneBakedModel(CGPaneBlock pane){
        this.pane = pane;
    }

    @Override
    public boolean isAmbientOcclusion(){
        return false;
    }

    @Override
    public boolean isGui3d(){
        return false;
    }

    @Override
    public boolean isBuiltInRenderer(){
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture(){
        return this.getTexture();
    }

    @Override
    public ItemOverrideList getOverrides(){
        return ItemOverrideList.NONE;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms(){
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.STONE.getDefaultState()).getItemCameraTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
        List<BakedQuad> quads = new ArrayList<>();

        CGPaneModelData data = state instanceof IExtendedBlockState ? ((IExtendedBlockState)state).getValue(CGPaneBlock.MODEL_DATA) : null;

        EnumFacing[] sides = side == null ? EnumFacing.values() : new EnumFacing[]{side};
        boolean culling = side != null;
        for(EnumFacing side2 : sides){
            quads.addAll(this.getPostQuad(side2, culling, this.isEnabledUp(null, data), this.isEnabledDown(null, data)));

            float[] uv = this.getUV(side2, data);
            for(EnumFacing part : EnumFacing.Plane.HORIZONTAL)
                quads.addAll(this.getPartQuad(state, part, side2, uv, culling, this.isEnabledUp(part, data), this.isEnabledDown(part, data)));
        }

        return quads;
    }

    protected TextureAtlasSprite getTexture(){
        return ClientProxy.TEXTURES.get(this.pane.block);
    }

    protected float[] getUV(EnumFacing side, CGPaneModelData modelData){
        if(side == EnumFacing.UP || side == EnumFacing.DOWN)
            return this.getBorderUV();
        return new float[]{0, 0, 16, 16};
    }

    protected List<BakedQuad> getPostQuad(EnumFacing side, boolean culling, boolean isEnabledUp, boolean isEnabledDown){
        if(side.getAxis() != EnumFacing.Axis.Y)
            return Collections.emptyList();

        boolean hasCulling = side == EnumFacing.UP ? isEnabledUp : isEnabledDown;
        if(hasCulling != culling)
            return Collections.emptyList();

        Vector3f from = new Vector3f(7, 0, 7), to = new Vector3f(9, 16, 9);
        float[] uv = new float[]{7 / 8f, 2 * 7 + 7 / 8f, 9 / 8f, 2 * 7 + 9 / 8f};
        BlockPartFace face = new BlockPartFace(hasCulling ? side : null, -1, "", new BlockFaceUV(uv, 0));

        BakedQuad quad = BAKERY.makeBakedQuad(from, to, face, getTexture(), side, ModelRotation.X0_Y0, null, false, true);
        return Collections.singletonList(quad);
    }

    protected List<BakedQuad> getPartQuad(IBlockState state, EnumFacing part, EnumFacing side, float[] totalUV, boolean culling, boolean isEnabledUp, boolean isEnabledDown){
        List<BakedQuad> quads = new ArrayList<>();
        float unitW = (totalUV[2] - totalUV[0]) / 16, unitH = (totalUV[3] - totalUV[1]) / 16; // width and height of one pixel

        PropertyBool property = part == EnumFacing.NORTH ? BlockPane.NORTH : part == EnumFacing.EAST ? BlockPane.EAST : part == EnumFacing.SOUTH ? BlockPane.SOUTH : part == EnumFacing.WEST ? BlockPane.WEST : null;
        if((state == null && (part == EnumFacing.NORTH || part == EnumFacing.SOUTH)) || (state != null && state.getValue(property))){

            boolean hasQuad = true;
            float[] uv = new float[0];
            boolean hasCulling = false;
            int rotation = 0;

            if(side == EnumFacing.UP || side == EnumFacing.DOWN){
                hasCulling = side == EnumFacing.UP ? isEnabledUp : isEnabledDown;
                if(part == EnumFacing.NORTH || part == EnumFacing.EAST)
                    uv = new float[]{totalUV[0] + 7 * unitW, totalUV[1], totalUV[0] + 9 * unitW, totalUV[1] + 7 * unitH};
                else
                    uv = new float[]{totalUV[0] + 7 * unitW, totalUV[1] + 9 * unitH, totalUV[0] + 9 * unitW, totalUV[3]};
                if(part.getAxis() == EnumFacing.Axis.X)
                    rotation = 90;
            }else if(side.getOpposite() == part)
                hasQuad = false;
            else if(side == part){
                uv = this.getBorderUV();
                uv[0] += 7 * unitW;
                uv[2] -= 7 * unitW;
                hasCulling = true;
            }else if(side.rotateYCCW() == part)
                uv = new float[]{totalUV[0] + 9 * unitW, totalUV[1], totalUV[2], totalUV[3]};
            else if(side.rotateY() == part)
                uv = new float[]{totalUV[0], totalUV[1], totalUV[0] + 7 * unitW, totalUV[3]};

            if(hasQuad && hasCulling == culling){
                Vector3f from = getPartFromPos(part), to = getPartToPos(part);
                BlockPartFace face = new BlockPartFace(hasCulling ? side : null, -1, "", new BlockFaceUV(uv, rotation));
                quads.add(BAKERY.makeBakedQuad(from, to, face, getTexture(), side, ModelRotation.X0_Y0, null, false, true));
            }
        }else if(side == part && !culling){
            Vector3f from = new Vector3f(7, 0, 7), to = new Vector3f(9, 16, 9);
            float[] uv = new float[]{totalUV[0] + 7 * unitW, totalUV[1], totalUV[0] + 9 * unitH, totalUV[3]};
            BlockPartFace face = new BlockPartFace(null, -1, "", new BlockFaceUV(uv, 0));
            quads.add(BAKERY.makeBakedQuad(from, to, face, getTexture(), side, ModelRotation.X0_Y0, null, false, true));
        }

        return quads;
    }

    private Vector3f getPartFromPos(EnumFacing part){
        if(part == EnumFacing.NORTH)
            return new Vector3f(7, 0, 0);
        else if(part == EnumFacing.EAST)
            return new Vector3f(9, 0, 7);
        else if(part == EnumFacing.SOUTH)
            return new Vector3f(7, 0, 9);
        else if(part == EnumFacing.WEST)
            return new Vector3f(0, 0, 7);
        return null;
    }

    private Vector3f getPartToPos(EnumFacing part){
        if(part == EnumFacing.NORTH)
            return new Vector3f(9, 16, 7);
        else if(part == EnumFacing.EAST)
            return new Vector3f(16, 16, 9);
        else if(part == EnumFacing.SOUTH)
            return new Vector3f(9, 16, 16);
        else if(part == EnumFacing.WEST)
            return new Vector3f(7, 16, 9);
        return null;
    }

    protected float[] getBorderUV(){
        return new float[]{0, 0, 16, 16};
    }

    protected boolean isEnabledUp(EnumFacing part, CGPaneModelData extraData){
        return false;
    }

    protected boolean isEnabledDown(EnumFacing part, CGPaneModelData extraData){
        return false;
    }

}
