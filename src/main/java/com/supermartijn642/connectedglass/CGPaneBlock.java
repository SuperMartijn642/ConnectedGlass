package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.model.CGConnectedPaneBakedModel;
import com.supermartijn642.connectedglass.model.CGPaneModelData;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends BlockPane {

    public static final IUnlistedProperty<CGPaneModelData> MODEL_DATA = new IUnlistedProperty<CGPaneModelData>() {
        @Override
        public String getName(){
            return "model_data";
        }

        @Override
        public boolean isValid(CGPaneModelData value){
            return true;
        }

        @Override
        public Class<CGPaneModelData> getType(){
            return CGPaneModelData.class;
        }

        @Override
        public String valueToString(CGPaneModelData value){
            return value.toString();
        }
    };

    public final CGGlassBlock block;

    public CGPaneBlock(CGGlassBlock block){
        super(Material.GLASS, true);
        this.block = block;
        this.setRegistryName(block.getRegistryName() + "_pane");
        this.setUnlocalizedName(this.getRegistryName().toString());

        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.3f);
        this.setResistance(0.3f);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos){
        return ((IExtendedBlockState)state).withProperty(MODEL_DATA, CGConnectedPaneBakedModel.getModelData(world, pos, state));
    }

    @Override
    protected BlockStateContainer createBlockState(){
        BlockStateContainer container = super.createBlockState();
        IProperty<?>[] properties = container.getProperties().toArray(new IProperty[0]);
        return new ExtendedBlockState(this, properties, new IUnlistedProperty[]{MODEL_DATA});
    }
}
