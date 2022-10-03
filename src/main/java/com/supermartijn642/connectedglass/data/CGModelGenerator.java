package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class CGModelGenerator extends ModelGenerator {

    public CGModelGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        // Create the glass block models
        for(CGGlassType type : CGGlassType.values()){
            this.cubeAll(type.getRegistryName(), new ResourceLocation("connectedglass", type.getRegistryName()));
            this.model("item/" + type.getRegistryName()).parent(type.getRegistryName());
            for(DyeColor color : DyeColor.values()){
                this.cubeAll(type.getRegistryName(color), new ResourceLocation("connectedglass", type.getRegistryName(color)));
                this.model("item/" + type.getRegistryName(color)).parent(type.getRegistryName(color));
            }
        }
        this.cubeAll("tinted_glass", new ResourceLocation("connectedglass", "tinted_glass"));
        this.model("item/tinted_glass").parent("tinted_glass");

        // Pane item model template
        this.model("pane_item_template")
            .parent("minecraft", "block/block")
            .particleTexture("#all")
            .element(element ->
                element.shape(7, 0, 0, 9, 16, 16)
                    .allFaces(face -> face.texture("#all"))
            );


        // Create the pane models
        for(CGGlassType type : CGGlassType.values()){
            if(type.hasPanes){
                this.createPaneModels(type.getPaneRegistryName(), type.getRegistryName());
                for(DyeColor color : DyeColor.values())
                    this.createPaneModels(type.getPaneRegistryName(color), type.getRegistryName(color));
            }
        }
    }

    private void createPaneModels(String identifier, String texture){
        this.model(identifier + "_side").parent("minecraft", "block/template_glass_pane_side").texture("pane", texture).texture("edge", texture);
        this.model(identifier + "_post").parent("minecraft", "block/template_glass_pane_post").texture("pane", texture).texture("edge", texture);
        this.model(identifier + "_side_alt").parent("minecraft", "block/template_glass_pane_side_alt").texture("pane", texture).texture("edge", texture);
        this.model(identifier + "_noside").parent("minecraft", "block/template_glass_pane_noside").texture("pane", texture).texture("edge", texture);
        this.model(identifier + "_noside_alt").parent("minecraft", "block/template_glass_pane_noside_alt").texture("pane", texture).texture("edge", texture);
        this.model("item/" + identifier).parent("pane_item_template").texture("all", texture);
    }
}
