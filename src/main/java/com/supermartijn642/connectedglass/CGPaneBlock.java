package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.model.CGModelData;
import com.supermartijn642.connectedglass.model.CGPaneModelData;
import com.supermartijn642.core.block.EditableBlockRenderLayer;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends BlockPane implements EditableBlockRenderLayer {

    public final CGGlassBlock block;
    private BlockRenderLayer renderLayer;

    public CGPaneBlock(CGGlassBlock block){
        super(Material.GLASS, false);
        this.block = block;

        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.3f);
        this.setResistance(0.3f);
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
        return ((IExtendedBlockState)state).withProperty(CGModelData.MODEL_PROPERTY, CGPaneModelData.create(level, pos, state));
    }

    protected BlockStateContainer createBlockState(){
        BlockStateContainer container = super.createBlockState();
        IProperty<?>[] properties = container.getProperties().toArray(new IProperty[0]);
        return new ExtendedBlockState(this, properties, new IUnlistedProperty[]{CGModelData.MODEL_PROPERTY});
    }

    public String getLocalizedName(){
        return I18n.format(this.getUnlocalizedName()).trim();
    }

    public String getUnlocalizedName(){
        return this.getRegistryName().getResourceDomain() + ".block." + this.getRegistryName().getResourcePath();
    }
}
