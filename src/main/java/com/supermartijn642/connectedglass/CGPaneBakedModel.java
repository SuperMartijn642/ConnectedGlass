package com.supermartijn642.connectedglass;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.supermartijn642.core.util.Pair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBakedModel extends BakedModelWrapper<BakedModel> {

    private static final int VERTEX_POSITION_OFFSET = findPositionOffset(DefaultVertexFormat.BLOCK);
    private static final ModelProperty<Pair<BlockState,BlockState>> MODEL_PROPERTY = new ModelProperty<>();

    public CGPaneBakedModel(BakedModel originalModel){
        super(originalModel);
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

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData){
        return super.getModelData(level, pos, state, modelData).derive()
            .with(MODEL_PROPERTY, Pair.of(level.getBlockState(pos.above()), level.getBlockState(pos.below())))
            .build();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType){
        if(state == null || !data.has(MODEL_PROPERTY))
            return super.getQuads(state, side, rand, data, renderType);

        // Gather the states above and below
        BlockState stateAbove = data.get(MODEL_PROPERTY).left();
        BlockState stateBelow = data.get(MODEL_PROPERTY).right();

        return super.getQuads(state, side, rand, data, renderType).stream()
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
