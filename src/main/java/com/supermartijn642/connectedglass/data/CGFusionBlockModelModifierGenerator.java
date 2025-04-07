package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.fusion.api.provider.FusionBlockModelModifierProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

/**
 * Created 07/04/2025 by SuperMartijn642
 */
public class CGFusionBlockModelModifierGenerator extends FusionBlockModelModifierProvider {

    public CGFusionBlockModelModifierGenerator(DataGenerator generator){
        super("connectedglass", generator);
    }

    @Override
    protected void generate(){
        // Use pane culling fix for all the pane models
        ModifierBuilder modifier = this.modifier(new ResourceLocation("connectedglass", "pane_culling_fix"));
        modifier.paneCullingFix(true);
        for(CGGlassType type : CGGlassType.values()){
            if(!type.hasPanes)
                continue;
            modifier.target(type.getPane());
            for(DyeColor color : DyeColor.values())
                modifier.target(type.getPane(color));
        }
    }
}
