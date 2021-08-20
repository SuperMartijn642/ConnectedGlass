package com.supermartijn642.connectedglass;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends AbstractGlassBlock {

    public final ResourceLocation texture;
    public final boolean connected;

    public CGGlassBlock(String registryName, String texture, boolean connected){
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f));
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos){
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos){
        return true;
    }

    @Override
    public boolean isRedstoneConductor(BlockState state, IBlockReader worldIn, BlockPos pos){
        return false;
    }

    @Override
    public boolean isValidSpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type){
        return false;
    }
}
