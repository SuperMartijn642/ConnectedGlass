package com.supermartijn642.connectedglass.model;

import com.mojang.math.Vector3f;
import com.supermartijn642.connectedglass.CGGlassBlock;
import com.supermartijn642.connectedglass.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGBakedModel implements IDynamicBakedModel {

    private static final FaceBakery BAKERY = new FaceBakery();

    private final CGGlassBlock block;

    public CGBakedModel(CGGlassBlock block){
        this.block = block;
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
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(Blocks.STONE.getRegistryName(), "")).getTransforms();
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData){
        if(side == null)
            return Collections.emptyList();

        return Collections.singletonList(this.createQuad(side, extraData));
    }

    protected TextureAtlasSprite getTexture(){
        return ClientProxy.TEXTURES.get(this.block);
    }

    protected BakedQuad createQuad(Direction side, IModelData modelData){
        BlockElementFace face = new BlockElementFace(side.getOpposite(), 0, "", new BlockFaceUV(this.getUV(side, modelData), 0));
        BakedQuad quad = BAKERY.bakeQuad(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16), face, getTexture(), side, BlockModelRotation.X0_Y0, null, true, null);
        return quad;
    }

    protected float[] getUV(Direction side, IModelData modelData){
        return new float[]{0, 0, 16, 16};
    }
}
