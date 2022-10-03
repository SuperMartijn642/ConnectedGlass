package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 6/24/2020 by SuperMartijn642
 */
public class CGLootTableGenerator extends LootTableGenerator {

    public CGLootTableGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        for(CGGlassType type : CGGlassType.values()){
            type.blocks.forEach(type.isTinted ? this::dropSelf : this::dropSelfWhenSilkTouch);
            if(type.hasPanes)
                type.panes.forEach(type.isTinted ? this::dropSelf : this::dropSelfWhenSilkTouch);
        }
    }
}
