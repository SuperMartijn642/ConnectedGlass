package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.AtlasSourceGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.render.TextureAtlases;
import net.minecraft.world.item.DyeColor;

/**
 * Created 13/11/2023 by SuperMartijn642
 */
public class CGAtlasSourceGenerator extends AtlasSourceGenerator {

    public CGAtlasSourceGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        AtlasBuilder blockAtlas = this.atlas(TextureAtlases.getBlocks());
        for(CGGlassType type : CGGlassType.values()){
            blockAtlas.texture(type.getRegistryName() + "/" + type.getRegistryName());
            blockAtlas.texture(type.getRegistryName() + "/" + type.getRegistryName() + "_edge");
            for(DyeColor color : DyeColor.values()){
                blockAtlas.texture(type.getRegistryName() + "/" + type.getRegistryName(color));
                blockAtlas.texture(type.getRegistryName() + "/" + type.getRegistryName(color) + "_edge");
            }
        }
    }
}
