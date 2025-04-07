package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.fusion.api.provider.FusionBlockModelModifierProvider;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

/**
 * Created 07/04/2025 by SuperMartijn642
 */
public class CGFusionBlockModelModifierGenerator extends FusionBlockModelModifierProvider {

    public CGFusionBlockModelModifierGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        // Use pane culling fix for all the pane models
        ModifierBuilder modifier = this.modifier(new ResourceLocation("connectedglass", "pane_culling_fix"));
        modifier.paneCullingFix(true);
        for(CGGlassType type : CGGlassType.values()){
            if(!type.hasPanes)
                continue;
            modifier.target(type.getPane());
            for(EnumDyeColor color : EnumDyeColor.values())
                modifier.target(type.getPane(color));
        }
    }
}
