package com.supermartijn642.connectedglass;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

/**
 * Created 5/16/2020 by SuperMartijn642
 */
public class CGDummyItemModelProvider extends ItemModelProvider {

    public CGDummyItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super(generator, "connectedglass", existingFileHelper);
    }

    @Override
    protected void registerModels(){
        for(CGGlassType type : CGGlassType.values()){
            for(CGGlassBlock block : type.blocks)
                getBuilder("item/" + block.getRegistryName().getPath());

            for(CGPaneBlock pane : type.panes)
                getBuilder("item/" + pane.getRegistryName().getPath());
        }
    }

    @Override
    public String getName(){
        return "connectedglass:dummyitemmodels";
    }
}
