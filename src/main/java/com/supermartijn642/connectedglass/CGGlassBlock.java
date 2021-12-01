package com.supermartijn642.connectedglass;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

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
    public boolean isValidSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType){
        return false;
    }
}
