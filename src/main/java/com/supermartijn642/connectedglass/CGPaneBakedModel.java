package com.supermartijn642.connectedglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBakedModel extends BakedModelWrapper<IBakedModel> {

    private static final int VERTEX_POSITION_OFFSET = findPositionOffset(DefaultVertexFormats.BLOCK);

    public CGPaneBakedModel(IBakedModel originalModel){
        super(originalModel);
    }

    @Override
    public org.apache.commons.lang3.tuple.Pair<? extends IBakedModel,Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType){
        return org.apache.commons.lang3.tuple.Pair.of(this, super.handlePerspective(cameraTransformType).getRight());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
        if(!(state instanceof IExtendedBlockState) || !((IExtendedBlockState)state).getUnlistedNames().contains(CGPaneBlock.PANE_MODEL_DATA))
            return super.getQuads(state, side, rand);

        // Gather the states above and below
        IBlockState stateAbove = ((IExtendedBlockState)state).getValue(CGPaneBlock.PANE_MODEL_DATA).left();
        IBlockState stateBelow = ((IExtendedBlockState)state).getValue(CGPaneBlock.PANE_MODEL_DATA).right();

        return super.getQuads(state, side, rand).stream()
            .filter(quad -> filterQuad(quad, state, stateAbove, stateBelow))
            .collect(Collectors.toList());
    }

    private static boolean filterQuad(BakedQuad quad, IBlockState self, IBlockState stateAbove, IBlockState stateBelow){
        EnumFacing quadDirection = quad.getFace();
        if(quadDirection != EnumFacing.UP && quadDirection != EnumFacing.DOWN)
            return true;

        float[] quadCenter = getQuadCenter(quad.getVertexData());
        double quadDistance = Math.sqrt((quadCenter[0] - 0.5) * (quadCenter[0] - 0.5) + (quadCenter[2] - 0.5) * (quadCenter[2] - 0.5));
        if(quadDistance < 0.1) // Centerpiece
            return quadDirection == EnumFacing.UP ? stateAbove.getBlock() != self.getBlock() : stateBelow.getBlock() != self.getBlock();

        EnumFacing partSide = EnumFacing.getFacingFromVector(quadCenter[0] - 0.5f, 0, quadCenter[2] - 0.5f);
        return quadDirection == EnumFacing.UP ?
            stateAbove.getBlock() != self.getBlock() || !stateAbove.getValue(CGPaneBlock.getConnectionProperty(partSide)) :
            stateBelow.getBlock() != self.getBlock() || !stateBelow.getValue(CGPaneBlock.getConnectionProperty(partSide));
    }

    private static float[] getQuadCenter(int[] vertexData){
        int vertexSize = DefaultVertexFormats.BLOCK.getIntegerSize();
        int vertices = vertexData.length / vertexSize;
        int positionOffset = VERTEX_POSITION_OFFSET / 4;
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
            if(el.getUsage() == VertexFormatElement.EnumUsage.POSITION){
                element = el;
                break;
            }
        }
        if(index == vertexFormat.getElements().size() || element == null)
            throw new RuntimeException("Expected vertex format to have a POSITION attribute");
        if(element.getType() != VertexFormatElement.EnumType.FLOAT)
            throw new RuntimeException("Expected POSITION attribute to have data type FLOAT");
        if(element.getSize() != 12)
            throw new RuntimeException("Expected POSITION attribute to have 3 dimensions");
        return vertexFormat.getOffset(index);
    }
}
