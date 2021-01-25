package com.supermartijn642.connectedglass;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends StainedGlassBlock {

    public final ResourceLocation texture;
    public final boolean connected;

    public CGGlassBlock(String registryName, String texture, boolean connected){
        super(DyeColor.WHITE, Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3f));
        this.texture = new ResourceLocation("connectedglass", texture);
        this.connected = connected;
        this.setRegistryName(registryName);
    }

    public CGGlassBlock(String registryName, boolean connected){
        this(registryName, registryName, connected);
    }

    public CGPaneBlock createPane(){
        return new CGPaneBlock(this);
    }

    @Override
    public BlockRenderLayer getRenderLayer(){
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos){
        return null;
    }
}
