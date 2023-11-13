package com.supermartijn642.connectedglass;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.supermartijn642.core.util.Triple;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBakedModel extends BakedModelWrapper<BakedModel> {

    private static final int VERTEX_POSITION_OFFSET = findPositionOffset(DefaultVertexFormat.BLOCK);
    private static final ModelProperty<Triple<BlockState,BlockState,IModelData>> MODEL_PROPERTY = new ModelProperty<>();

    public CGPaneBakedModel(BakedModel originalModel){
        super(originalModel);
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack){
        super.handlePerspective(cameraTransformType, poseStack);
        return this;
    }

    @Override
    public @Nonnull IModelData getModelData(@Nonnull BlockAndTintGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData modelData){
        return new ModelDataMap.Builder()
            .withInitial(MODEL_PROPERTY, Triple.of(level.getBlockState(pos.above()), level.getBlockState(pos.below()), super.getModelData(level, pos, state, modelData)))
            .build();
    }

    @Override
    public @Nonnull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data){
        if(state == null || !data.hasProperty(MODEL_PROPERTY))
            return super.getQuads(state, side, rand, data);

        // Gather the states above and below
        BlockState stateAbove = data.getData(MODEL_PROPERTY).left();
        BlockState stateBelow = data.getData(MODEL_PROPERTY).middle();
        data = data.getData(MODEL_PROPERTY).right();

        return super.getQuads(state, side, rand, data).stream()
            .filter(quad -> filterQuad(quad, state, stateAbove, stateBelow))
            .collect(Collectors.toList());
    }

    private static boolean filterQuad(BakedQuad quad, BlockState self, BlockState stateAbove, BlockState stateBelow){
        Direction quadDirection = quad.getDirection();
        if(quadDirection != Direction.UP && quadDirection != Direction.DOWN)
            return true;

        float[] quadCenter = getQuadCenter(quad.getVertices());
        double quadDistance = Math.sqrt((quadCenter[0] - 0.5) * (quadCenter[0] - 0.5) + (quadCenter[2] - 0.5) * (quadCenter[2] - 0.5));
        if(quadDistance < 0.1) // Centerpiece
            return quadDirection == Direction.UP ? stateAbove.getBlock() != self.getBlock() : stateBelow.getBlock() != self.getBlock();

        Direction partSide = Direction.getNearest(quadCenter[0] - 0.5f, 0, quadCenter[2] - 0.5f);
        return quadDirection == Direction.UP ?
            stateAbove.getBlock() != self.getBlock() || !stateAbove.getValue(CGPaneBlock.getConnectionProperty(partSide)) :
            stateBelow.getBlock() != self.getBlock() || !stateBelow.getValue(CGPaneBlock.getConnectionProperty(partSide));
    }

    private static float[] getQuadCenter(int[] vertexData){
        int vertexSize = DefaultVertexFormat.BLOCK.getIntegerSize();
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
