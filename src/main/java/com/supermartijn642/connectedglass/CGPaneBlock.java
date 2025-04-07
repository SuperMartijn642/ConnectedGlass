package com.supermartijn642.connectedglass;

import com.supermartijn642.core.block.EditableBlockRenderLayer;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.BlockRenderLayer;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends BlockPane implements EditableBlockRenderLayer {

    public final CGGlassBlock block;
    private BlockRenderLayer renderLayer;

    public CGPaneBlock(CGGlassBlock block){
        super(Material.GLASS, false);
        this.block = block;

        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.3f);
        this.setResistance(0.3f);
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
