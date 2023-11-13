package com.supermartijn642.connectedglass;

import com.supermartijn642.core.block.EditableBlockRenderLayer;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends BlockGlass implements EditableBlockRenderLayer {

    public final ResourceLocation texture;
    public final boolean connected;
    private BlockRenderLayer renderLayer;

    public CGGlassBlock(String texture, boolean connected){
        super(Material.GLASS, false);
        this.texture = new ResourceLocation("connectedglass", texture);
        this.connected = connected;

        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.3f);
        this.setResistance(0.3f);
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess level, BlockPos pos, EntityLiving.SpawnPlacementType type){
        return false;
    }

    @Override
    public void setRenderLayer(BlockRenderLayer layer){
        this.renderLayer = layer;
    }

    @Override
    public BlockRenderLayer getBlockLayer(){
        return this.renderLayer;
    }

    public String getLocalizedName(){
        return I18n.format(this.getUnlocalizedName()).trim();
    }

    public String getUnlocalizedName(){
        return this.getRegistryName().getResourceDomain() + ".block." + this.getRegistryName().getResourcePath();
    }
}
