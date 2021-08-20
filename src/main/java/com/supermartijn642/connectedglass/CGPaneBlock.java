package com.supermartijn642.connectedglass;

import net.minecraft.block.PaneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends PaneBlock {

    public final CGGlassBlock block;

    public CGPaneBlock(CGGlassBlock block){
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f));
        this.block = block;
        this.setRegistryName(block.getRegistryName().getPath() + "_pane");
    }

    @Override
    public BlockRenderLayer getRenderLayer(){
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
