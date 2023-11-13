package com.supermartijn642.connectedglass;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBakedModel extends ForwardingBakedModel {

    public CGPaneBakedModel(BakedModel originalModel){
        this.wrapped = originalModel;
    }

    @Override
    public boolean isVanillaAdapter(){
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context){
        // Gather the states above and below
        BlockState stateAbove = blockView.getBlockState(pos.above());
        BlockState stateBelow = blockView.getBlockState(pos.below());

        // Filter out certain quads
        context.pushTransform(quad -> filterQuad(quad, state, stateAbove, stateBelow));
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }

    private static boolean filterQuad(QuadView quad, BlockState self, BlockState stateAbove, BlockState stateBelow){
        Direction quadDirection = quad.nominalFace();
        if(quadDirection != Direction.UP && quadDirection != Direction.DOWN)
            return true;

        float[] quadCenter = getQuadCenter(quad);
        double quadDistance = Math.sqrt((quadCenter[0] - 0.5) * (quadCenter[0] - 0.5) + (quadCenter[2] - 0.5) * (quadCenter[2] - 0.5));
        if(quadDistance < 0.1) // Centerpiece
            return quadDirection == Direction.UP ? stateAbove.getBlock() != self.getBlock() : stateBelow.getBlock() != self.getBlock();

        Direction partSide = Direction.getNearest(quadCenter[0] - 0.5f, 0, quadCenter[2] - 0.5f);
        return quadDirection == Direction.UP ?
            stateAbove.getBlock() != self.getBlock() || !stateAbove.getValue(CGPaneBlock.getConnectionProperty(partSide)) :
            stateBelow.getBlock() != self.getBlock() || !stateBelow.getValue(CGPaneBlock.getConnectionProperty(partSide));
    }

    private static float[] getQuadCenter(QuadView quad){
        float averageX = 0, averageY = 0, averageZ = 0;

        for(int i = 0; i < 4; i++){
            averageX += quad.x(i);
            averageY += quad.y(i);
            averageZ += quad.z(i);
        }

        averageX /= 4;
        averageY /= 4;
        averageZ /= 4;
        return new float[]{averageX, averageY, averageZ};
    }
}
