package com.supermartijn642.connectedglass;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.EditableBlockRenderLayer;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends BlockPane implements EditableBlockRenderLayer {

    public static final IUnlistedProperty<Pair<IBlockState,IBlockState>> PANE_MODEL_DATA = new IUnlistedProperty<Pair<IBlockState,IBlockState>>() {
        @Override
        public String getName(){
            return "model_data";
        }

        @Override
        public boolean isValid(Pair<IBlockState,IBlockState> value){
            return true;
        }

        @Override
        public Class<Pair<IBlockState,IBlockState>> getType(){
            //noinspection unchecked
            return (Class<Pair<IBlockState,IBlockState>>)(Object)Pair.class;
        }

        @Override
        public String valueToString(Pair<IBlockState,IBlockState> value){
            return value.toString();
        }
    };
    private static final PropertyBool[] PROPERTY_BY_DIRECTION = new PropertyBool[]{null, null, NORTH, SOUTH, WEST, EAST};

    public final CGGlassBlock block;
    private BlockRenderLayer renderLayer;

    public CGPaneBlock(CGGlassBlock block){
        super(Material.GLASS, false);
        this.block = block;

        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.3f);
        this.setResistance(0.3f);
    }

    public static PropertyBool getConnectionProperty(EnumFacing side){
        return PROPERTY_BY_DIRECTION[side.getIndex()];
    }

    @Override
    public void setRenderLayer(BlockRenderLayer layer){
        this.renderLayer = layer;
    }

    @Override
    public BlockRenderLayer getBlockLayer(){
        return this.renderLayer;
    }

    public IBlockState getExtendedState(IBlockState state, IBlockAccess level, BlockPos pos){
        if(CommonUtils.getEnvironmentSide().isServer())
            return state;
        return ((IExtendedBlockState)state).withProperty(PANE_MODEL_DATA, Pair.of(level.getBlockState(pos.up()).getActualState(level, pos.up()), level.getBlockState(pos.down()).getActualState(level, pos.down())));
    }

    protected BlockStateContainer createBlockState(){
        BlockStateContainer container = super.createBlockState();
        IProperty<?>[] properties = container.getProperties().toArray(new IProperty[0]);
        return new ExtendedBlockState(this, properties, new IUnlistedProperty[]{PANE_MODEL_DATA});
    }

    public String getLocalizedName(){
        return I18n.format(this.getUnlocalizedName()).trim();
    }

    public String getUnlocalizedName(){
        return this.getRegistryName().getResourceDomain() + ".block." + this.getRegistryName().getResourcePath();
    }
}
