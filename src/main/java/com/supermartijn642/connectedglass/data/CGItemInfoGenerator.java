package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ItemInfoGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.world.item.DyeColor;

/**
 * Created 08/04/2025 by SuperMartijn642
 */
public class CGItemInfoGenerator extends ItemInfoGenerator {

    public CGItemInfoGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        // Glass blocks
        for(CGGlassType type : CGGlassType.values()){
            this.simpleInfo(type.getBlock(), type.getRegistryName());
            for(DyeColor color : DyeColor.values())
                this.simpleInfo(type.getBlock(color), type.getRegistryName(color));
        }

        // Glass panes
        for(CGGlassType type : CGGlassType.values()){
            if(type.hasPanes){
                this.simpleInfo(type.getPane(), "item/" + type.getPaneRegistryName());
                for(DyeColor color : DyeColor.values())
                    this.simpleInfo(type.getPane(color), "item/" + type.getPaneRegistryName(color));
            }
        }
    }
}
