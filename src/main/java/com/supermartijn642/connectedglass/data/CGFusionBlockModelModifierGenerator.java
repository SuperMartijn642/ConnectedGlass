package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.fusion.api.provider.FusionBlockModelModifierProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/**
 * Created 07/04/2025 by SuperMartijn642
 */
public class CGFusionBlockModelModifierGenerator extends FusionBlockModelModifierProvider {

    public CGFusionBlockModelModifierGenerator(PackOutput output){
        super("connectedglass", output);
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
