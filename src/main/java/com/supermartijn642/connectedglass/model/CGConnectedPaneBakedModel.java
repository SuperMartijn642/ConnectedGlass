package com.supermartijn642.connectedglass.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGConnectedPaneBakedModel extends CGConnectedBakedModel {

    public CGConnectedPaneBakedModel(IBakedModel originalModel){
        super(originalModel);
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData){
        return new ModelDataMap.Builder().withInitial(CGModelData.MODEL_PROPERTY, CGPaneModelData.create(level, pos, state)).build();
    }

    @Override
    protected BakedQuad remapQuad(BakedQuad quad, CGModelData modelData){
        Direction quadDirection = quad.getDirection();
        boolean isUpOrDown = quadDirection == Direction.UP || quadDirection == Direction.DOWN;
        float[] quadCenter = this.getQuadCenter(quad.getVertices());
        double quadDistance = Math.sqrt((quadCenter[0] - 0.5) * (quadCenter[0] - 0.5) + (quadCenter[2] - 0.5) * (quadCenter[2] - 0.5));

        if(isUpOrDown && modelData instanceof CGPaneModelData){
            Direction partSide = Direction.getNearest(quadCenter[0] - 0.5f, 0, quadCenter[2] - 0.5f);
            if(quadDistance < 0.1 ? quadDirection == Direction.UP ? ((CGPaneModelData)modelData).isAbovePane() : ((CGPaneModelData)modelData).isBelowPane() :
                quadDirection == Direction.UP ? ((CGPaneModelData)modelData).isAboveConnectedTo(partSide) : ((CGPaneModelData)modelData).isBelowConnectedTo(partSide))
                return null;
        }

        int[] vertexData = quad.getVertices();
        // Make sure we don't change the original quad
        vertexData = Arrays.copyOf(vertexData, vertexData.length);

        // Adjust the uv
        int[] newUV = isUpOrDown || quadDistance > 0.4 ? new int[]{0, 7} : this.getUV(modelData == null ? null : modelData.getSideData(quadDirection == Direction.NORTH ? Direction.SOUTH : quadDirection == Direction.WEST ? Direction.EAST : quadDirection));
        adjustVertexDataUV(vertexData, newUV[0], newUV[1], quad.getSprite(), DefaultVertexFormats.BLOCK);

        // Create a new quad
        return new BakedQuad(vertexData, quad.getTintIndex(), quadDirection, quad.getSprite(), quad.isShade());
    }

    private float[] getQuadCenter(int[] vertexData){
        int vertexSize = DefaultVertexFormats.BLOCK.getIntegerSize();
        int vertices = vertexData.length / vertexSize;
        int positionOffset = findPositionOffset(DefaultVertexFormats.BLOCK) / 4;

        float averageX = 0, averageY = 0, averageZ = 0;

        for(int i = 0; i < vertices; i++){
            int offset = i * vertexSize + positionOffset;

            averageX += Float.intBitsToFloat(vertexData[offset]);
            averageY += Float.intBitsToFloat(vertexData[offset + 1]);
            averageZ += Float.intBitsToFloat(vertexData[offset + 2]);
        }

        averageX /= vertices;
        averageY /= vertices;
        averageZ /= vertices;
        return new float[]{averageX, averageY, averageZ};
    }

    private static int findPositionOffset(VertexFormat vertexFormat){
        int index;
        VertexFormatElement element = null;
        for(index = 0; index < vertexFormat.getElements().size(); index++){
            VertexFormatElement el = vertexFormat.getElements().get(index);
            if(el.getUsage() == VertexFormatElement.Usage.POSITION){
                element = el;
                break;
            }
        }
        if(index == vertexFormat.getElements().size() || element == null)
            throw new RuntimeException("Expected vertex format to have a POSITION attribute");
        if(element.getType() != VertexFormatElement.Type.FLOAT)
            throw new RuntimeException("Expected POSITION attribute to have data type FLOAT");
        if(element.getByteSize() != 12)
            throw new RuntimeException("Expected POSITION attribute to have 3 dimensions");
        return vertexFormat.getOffset(index);
    }
}
