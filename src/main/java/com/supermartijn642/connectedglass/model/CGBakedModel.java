package com.supermartijn642.connectedglass.model;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGBakedModel extends BakedModelWrapper<BakedModel> {

    // [cullface][hashcode * 6]
    private final Map<Direction,Map<Integer,List<BakedQuad>>> quadCache = new HashMap<>();
    private final Map<Integer,List<BakedQuad>> directionlessQuadCache = new HashMap<>();
    private final TextureAtlasSprite particleSprite;

    public CGBakedModel(BakedModel original){
        super(original);
        for(Direction direction : Direction.values())
            this.quadCache.put(direction, new HashMap<>());
        this.particleSprite = new CroppedTextureAtlasSprite(original.getParticleIcon());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand){
        return this.getQuads(state, side, rand, ModelData.EMPTY, null);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData modelData, RenderType renderType){
        CGModelData data = modelData.has(CGModelData.MODEL_PROPERTY) ? modelData.get(CGModelData.MODEL_PROPERTY) : null;
        int hashCode = data == null ? 0 : data.hashCode();

        // Compute the quads if they aren't in the cache yet
        Map<Integer,List<BakedQuad>> cache = side == null ? this.directionlessQuadCache : this.quadCache.get(side);
        if(!cache.containsKey(hashCode)){
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized(cache){
                if(!cache.containsKey(hashCode))
                    cache.put(hashCode, this.remapQuads(this.originalModel.getQuads(state, side, rand, modelData, renderType), data));
            }
        }

        return cache.get(hashCode);
    }

    private List<BakedQuad> remapQuads(List<BakedQuad> originalQuads, CGModelData modelData){
        return originalQuads.stream().map(quad -> this.remapQuad(quad, modelData)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected BakedQuad remapQuad(BakedQuad quad, CGModelData modelData){
        int[] vertexData = quad.getVertices();
        // Make sure we don't change the original quad
        vertexData = Arrays.copyOf(vertexData, vertexData.length);

        // Adjust the uv
        adjustVertexDataUV(vertexData, 0, 0, quad.getSprite(), DefaultVertexFormat.BLOCK);

        // Create a new quad
        return new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade());
    }

    public static int[] adjustVertexDataUV(int[] vertexData, int newU, int newV, TextureAtlasSprite sprite, VertexFormat vertexFormat){
        int vertexSize = vertexFormat.getIntegerSize();
        int vertices = vertexData.length / vertexSize;
        int uvOffset = findUVOffset(vertexFormat) / 4;

        for(int i = 0; i < vertices; i++){
            int offset = i * vertexSize + uvOffset;

            float width = sprite.getU1() - sprite.getU0();
            float u = (newU + (Float.intBitsToFloat(vertexData[offset]) - sprite.getU0()) / width) * 2;
            vertexData[offset] = Float.floatToRawIntBits(sprite.getU(u));

            float height = sprite.getV1() - sprite.getV0();
            float v = (newV + (Float.intBitsToFloat(vertexData[offset + 1]) - sprite.getV0()) / height) * 2;
            vertexData[offset + 1] = Float.floatToRawIntBits(sprite.getV(v));
        }
        return vertexData;
    }

    private static int findUVOffset(VertexFormat vertexFormat){
        int index;
        VertexFormatElement element = null;
        for(index = 0; index < vertexFormat.getElements().size(); index++){
            VertexFormatElement el = vertexFormat.getElements().get(index);
            if(el.getUsage() == VertexFormatElement.Usage.UV){
                element = el;
                break;
            }
        }
        if(index == vertexFormat.getElements().size() || element == null)
            throw new RuntimeException("Expected vertex format to have a UV attribute");
        if(element.getType() != VertexFormatElement.Type.FLOAT)
            throw new RuntimeException("Expected UV attribute to have data type FLOAT");
        if(element.getByteSize() < 4)
            throw new RuntimeException("Expected UV attribute to have at least 4 dimensions");
        return vertexFormat.getOffset(index);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(){
        return this.particleSprite;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform){
        super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return this;
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous){
        return List.of(this);
    }

    private static class CroppedTextureAtlasSprite extends TextureAtlasSprite {

        protected CroppedTextureAtlasSprite(TextureAtlasSprite original){
            super(
                original.atlasLocation(),
                new SpriteContents(original.contents().name(), new FrameSize(original.contents().width() / 8, original.contents().height() / 8), new NativeImage(original.contents().width() / 8, original.contents().height() / 8, false), new AnimationMetadataSection(List.of(), original.contents().width(), original.contents().height(), 0, false), original.contents().forgeMeta),
                Math.round(original.getX() / original.getU0()),
                Math.round(original.getY() / original.getV0()),
                original.getX(),
                original.getY()
            );
        }
    }
}
