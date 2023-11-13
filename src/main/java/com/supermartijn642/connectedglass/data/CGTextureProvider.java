package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.api.ChiseledTextureProvider;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

/**
 * Created 13/11/2023 by SuperMartijn642
 */
public class CGTextureProvider extends ChiseledTextureProvider {

    public CGTextureProvider(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    protected void createTextures(){
        for(EnumDyeColor color : EnumDyeColor.values()){
            if(color == EnumDyeColor.RED)
                continue;
            String colorName = color == EnumDyeColor.SILVER ? "light_gray" : color.getName();
            PaletteMap paletteMap = this.createPaletteMap(new ResourceLocation("connectedglass", "palettes/red_palette"), new ResourceLocation("connectedglass", "palettes/" + colorName + "_palette"));
            for(CGGlassType type : CGGlassType.values()){
                paletteMap.applyToTexture(new ResourceLocation("connectedglass", type.getRegistryName() + "/" + type.getRegistryName() + "_red"), type.getRegistryName() + "/" + type.getRegistryName(color));
                paletteMap.applyToTexture(new ResourceLocation("connectedglass", type.getRegistryName() + "/" + type.getRegistryName() + "_red_edge"), type.getRegistryName() + "/" + type.getRegistryName(color) + "_edge");
            }
        }
    }
}
