package com.supermartijn642.connectedglass;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends IronBarsBlock {

    public final CGGlassBlock block;

    public CGPaneBlock(CGGlassBlock block){
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f).noOcclusion());
        this.block = block;
        this.setRegistryName(block.getRegistryName().getPath() + "_pane");
    }
}
