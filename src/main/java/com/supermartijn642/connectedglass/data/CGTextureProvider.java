package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.rechiseled.api.ChiseledTextureProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/**
 * Created 13/11/2023 by SuperMartijn642
 */
public class CGTextureProvider extends ChiseledTextureProvider {

    public CGTextureProvider(FabricDataOutput generator){
        super("connectedglass", generator);
    }

    @Override
    protected void createTextures(){
        for(DyeColor color : DyeColor.values()){
            if(color == DyeColor.RED)
                continue;
            String colorName = color.getName();
            PaletteMap paletteMap = this.createPaletteMap(new ResourceLocation("connectedglass", "palettes/red_palette"), new ResourceLocation("connectedglass", "palettes/" + colorName + "_palette"));
            for(CGGlassType type : CGGlassType.values()){
                paletteMap.applyToTexture(new ResourceLocation("connectedglass", type.getRegistryName() + "/" + type.getRegistryName() + "_red"), type.getRegistryName() + "/" + type.getRegistryName(color));
                paletteMap.applyToTexture(new ResourceLocation("connectedglass", type.getRegistryName() + "/" + type.getRegistryName() + "_red_edge"), type.getRegistryName() + "/" + type.getRegistryName(color) + "_edge");
            }
        }
    }
}
