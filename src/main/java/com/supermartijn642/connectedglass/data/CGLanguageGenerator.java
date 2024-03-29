package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.ConnectedGlass;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.item.DyeColor;

/**
 * Created 03/10/2022 by SuperMartijn642
 */
public class CGLanguageGenerator extends LanguageGenerator {

    public CGLanguageGenerator(ResourceCache cache){
        super("connectedglass", cache, "en_us");
    }

    @Override
    public void generate(){
        this.itemGroup(ConnectedGlass.GROUP, "Connected Glass");

        this.block(ConnectedGlass.tinted_glass, "Tinted Glass");
        for(CGGlassType type : CGGlassType.values()){
            this.block(type.getBlock(), type.translation + " Glass");
            if(type.hasPanes)
                this.block(type.getPane(), type.translation + " Glass Pane");
            for(DyeColor color : DyeColor.values()){
                this.block(type.getBlock(color), type.translation + " " + TextComponents.translation("color.minecraft." + color.getName()).format() + " Stained Glass");
                if(type.hasPanes)
                    this.block(type.getPane(color), type.translation + " " + TextComponents.translation("color.minecraft." + color.getName()).format() + " Stained Glass Pane");
            }
        }
    }
}
