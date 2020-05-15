package com.supermartijn642.connectedglass.model;

import com.supermartijn642.connectedglass.CGGlassBlock;
import com.supermartijn642.connectedglass.ClientProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGBakedModel implements IBakedModel {

    private static final FaceBakery BAKERY = new FaceBakery();

    private final CGGlassBlock block;

    public CGBakedModel(CGGlassBlock block){
        this.block = block;
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

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
        if(side == null)
            return Collections.emptyList();

        CGModelData data = state instanceof IExtendedBlockState ? ((IExtendedBlockState)state).getValue(CGGlassBlock.MODEL_DATA) : null;

        return Collections.singletonList(this.createQuad(side, data));
    }

    protected TextureAtlasSprite getTexture(){
        return ClientProxy.TEXTURES.get(this.block);
    }

    protected BakedQuad createQuad(EnumFacing side, CGModelData modelData){
        BlockPartFace face = new BlockPartFace(side.getOpposite(), 0, "", new BlockFaceUV(this.getUV(side, modelData), 0));
        BakedQuad quad = BAKERY.makeBakedQuad(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16), face, getTexture(), side, ModelRotation.X0_Y0, null, false, true);
        return quad;
    }

    protected float[] getUV(EnumFacing side, CGModelData modelData){
        return new float[]{0, 0, 16, 16};
    }
}
