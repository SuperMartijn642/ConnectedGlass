package com.supermartijn642.connectedglass.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGBakedModel extends BakedModelWrapper<IBakedModel> {

    // [cullface][hashcode * 6]
    private final Map<EnumFacing,Map<Integer,List<BakedQuad>>> quadCache = new HashMap<>();
    private final Map<Integer,List<BakedQuad>> directionlessQuadCache = new HashMap<>();
    private final TextureAtlasSprite particleSprite;

    public CGBakedModel(IBakedModel original){
        super(original);
        for(EnumFacing direction : EnumFacing.values())
            this.quadCache.put(direction, new HashMap<>());
        this.particleSprite = new CroppedTextureAtlasSprite(original.getParticleTexture());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
        CGModelData data = state instanceof IExtendedBlockState && ((IExtendedBlockState)state).getUnlistedNames().contains(CGModelData.MODEL_PROPERTY) ? ((IExtendedBlockState)state).getValue(CGModelData.MODEL_PROPERTY) : null;
        int hashCode = data == null ? 0 : data.hashCode();

        // Compute the quads if they aren't in the cache yet
        Map<Integer,List<BakedQuad>> cache = side == null ? this.directionlessQuadCache : this.quadCache.get(side);
        if(!cache.containsKey(hashCode)){
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized(cache){
                if(!cache.containsKey(hashCode))
                    cache.put(hashCode, this.remapQuads(this.originalModel.getQuads(state, side, rand), data));
            }
        }

        return cache.get(hashCode);
    }

    private List<BakedQuad> remapQuads(List<BakedQuad> originalQuads, CGModelData modelData){
        return originalQuads.stream().map(quad -> this.remapQuad(quad, modelData)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected BakedQuad remapQuad(BakedQuad quad, CGModelData modelData){
        int[] vertexData = quad.getVertexData();
        // Make sure we don't change the original quad
        vertexData = Arrays.copyOf(vertexData, vertexData.length);

        // Adjust the uv
//        adjustVertexDataUV(vertexData, 0, 0, quad.getSprite(), quad.getFormat());

        // Create a new quad
        return new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
    }

    public static int[] adjustVertexDataUV(int[] vertexData, int newU, int newV, TextureAtlasSprite sprite, VertexFormat vertexFormat){
        int vertexSize = vertexFormat.getIntegerSize();
        int vertices = vertexData.length / vertexSize;
        int uvOffset = findUVOffset(vertexFormat) / 4;

        for(int i = 0; i < vertices; i++){
            int offset = i * vertexSize + uvOffset;

            float width = sprite.getMaxU() - sprite.getMinU();
            float u = (newU + (Float.intBitsToFloat(vertexData[offset]) - sprite.getMinU()) / width) * 2;
            vertexData[offset] = Float.floatToRawIntBits(sprite.getInterpolatedU(u));

            float height = sprite.getMaxV() - sprite.getMinV();
            float v = (newV + (Float.intBitsToFloat(vertexData[offset + 1]) - sprite.getMinV()) / height) * 2;
            vertexData[offset + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(v));
        }
        return vertexData;
    }

    private static int findUVOffset(VertexFormat vertexFormat){
        int index;
        VertexFormatElement element = null;
        for(index = 0; index < vertexFormat.getElements().size(); index++){
            VertexFormatElement el = vertexFormat.getElements().get(index);
            if(el.getUsage() == VertexFormatElement.EnumUsage.UV){
                element = el;
                break;
            }
        }
        if(index == vertexFormat.getElements().size() || element == null)
            throw new RuntimeException("Expected vertex format to have a UV attribute");
        if(element.getType() != VertexFormatElement.EnumType.FLOAT)
            throw new RuntimeException("Expected UV attribute to have data type FLOAT");
        if(element.getSize() < 4)
            throw new RuntimeException("Expected UV attribute to have at least 4 dimensions");
        return vertexFormat.getOffset(index);
    }

    @Override
    public TextureAtlasSprite getParticleTexture(){
        return this.particleSprite;
    }

    @Override
    public Pair<? extends IBakedModel,Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType){
        return Pair.of(this, super.handlePerspective(cameraTransformType).getRight());
    }

    private static class CroppedTextureAtlasSprite extends TextureAtlasSprite {

        protected CroppedTextureAtlasSprite(TextureAtlasSprite original){
            super(original.getIconName());
            this.setIconWidth(original.getIconWidth() / 8);
            this.setIconHeight(original.getIconHeight() / 8);
            this.initSprite(Math.round(original.getOriginX() / original.getMinU()), Math.round(original.getOriginY() / original.getMinV()), original.getOriginX(), original.getOriginY(), false);
        }
    }
}
