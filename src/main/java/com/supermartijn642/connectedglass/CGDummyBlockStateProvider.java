package com.supermartijn642.connectedglass;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

/**
 * Created 5/16/2020 by SuperMartijn642
 */
public class CGDummyBlockStateProvider extends BlockStateProvider {

    public CGDummyBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper){
        super(gen, "connectedglass", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels(){
        ModelFile model = this.getExistingFile(new ResourceLocation("minecraft", "block/cobblestone"));
        for(CGGlassType type : CGGlassType.values()){
            for(CGGlassBlock block : type.blocks)
                this.simpleBlock(block, model);

            for(CGPaneBlock pane : type.panes)
                this.simpleBlock(pane, model);
        }

        this.simpleBlock(ConnectedGlass.tinted_glass, model);
    }
}
