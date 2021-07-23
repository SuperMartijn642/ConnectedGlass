package com.supermartijn642.connectedglass;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends AbstractGlassBlock {

    public final ResourceLocation texture;
    public final boolean connected;

    public CGGlassBlock(String registryName, String texture, boolean connected){
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f).noOcclusion());
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
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, EntityType<?> entityType){
        return false;
    }
}
