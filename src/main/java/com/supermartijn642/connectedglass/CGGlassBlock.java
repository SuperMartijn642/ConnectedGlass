package com.supermartijn642.connectedglass;

import com.supermartijn642.core.block.EditableBlockRenderLayer;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends AbstractGlassBlock implements EditableBlockRenderLayer {

    public final ResourceLocation texture;
    public final boolean connected;
    private BlockRenderLayer renderLayer;

    public CGGlassBlock(String texture, boolean connected){
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f));
        this.texture = new ResourceLocation("connectedglass", texture);
        this.connected = connected;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader level, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, EntityType<?> entityType){
        return false;
    }

    @Override
    public void setRenderLayer(BlockRenderLayer layer){
        this.renderLayer = layer;
    }

    @Override
    public BlockRenderLayer getRenderLayer(){
        return this.renderLayer;
    }
}
