package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.model.CGConnectedBakedModel;
import com.supermartijn642.connectedglass.model.CGConnectedPaneBakedModel;
import com.supermartijn642.connectedglass.model.CGModelData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends BlockGlass {

    public static final IUnlistedProperty<CGModelData> MODEL_DATA = new IUnlistedProperty<CGModelData>() {
        @Override
        public String getName(){
            return "model_data";
        }

        @Override
        public boolean isValid(CGModelData value){
            return true;
        }

        @Override
        public Class<CGModelData> getType(){
            return CGModelData.class;
        }

        @Override
        public String valueToString(CGModelData value){
            return value.toString();
        }
    };

    public final ResourceLocation texture;
    public final boolean connected;

    public CGGlassBlock(String registryName, String texture, boolean connected){
        super(Material.GLASS, false);
        this.texture = new ResourceLocation("connectedglass", texture);
        this.connected = connected;
        this.setRegistryName(registryName);
        this.setUnlocalizedName(this.getRegistryName().toString());

        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.3f);
        this.setResistance(0.3f);
    }

    public CGGlassBlock(String registryName, boolean connected){
        this(registryName, registryName, connected);
    }

    public CGPaneBlock createPane(){
        return new CGPaneBlock(this);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos){
        return ((IExtendedBlockState)state).withProperty(MODEL_DATA, CGConnectedBakedModel.getModelData(world, pos, state));
    }

    @Override
    protected BlockStateContainer createBlockState(){
        BlockStateContainer container = super.createBlockState();
        IProperty<?>[] properties = container.getProperties().toArray(new IProperty[0]);
        return new ExtendedBlockState(this, properties, new IUnlistedProperty[]{MODEL_DATA});
    }
}
