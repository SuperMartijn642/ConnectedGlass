package com.supermartijn642.connectedglass;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

import javax.annotation.Nonnull;

/**
 * Created 5/16/2020 by SuperMartijn642
 */
public class CGDummyBlockStateProvider extends BlockStateProvider {

    public CGDummyBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper){
        super(gen, "connectedglass", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels(){
        ModelFile model = models().getExistingFile(new ResourceLocation("minecraft", "block/cobblestone"));
        for(CGGlassType type : CGGlassType.values()){
            for(CGGlassBlock block : type.blocks)
                simpleBlock(block, model);

            for(CGPaneBlock pane : type.panes)
                simpleBlock(pane, model);
        }
    }

    @Nonnull
    @Override
    public String getName(){
        return "connectedglass:dummyblockstates";
    }
}
