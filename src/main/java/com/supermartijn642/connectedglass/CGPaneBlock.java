package com.supermartijn642.connectedglass;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends IronBarsBlock {

    private final ResourceLocation registryName;

    public final CGGlassBlock block;

    public CGPaneBlock(CGGlassBlock block){
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f).noOcclusion());
        this.registryName = new ResourceLocation(ModLoadingContext.get().getActiveNamespace(), block.getRegistryName().getPath() + "_pane");
        this.block = block;
    }

    public ResourceLocation getRegistryName(){
        return this.registryName;
    }
}
