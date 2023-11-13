package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

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
            this.model("item/" + type.getRegistryName()).parent(type.getRegistryName());
            for(EnumDyeColor color : EnumDyeColor.values())
                this.model("item/" + type.getRegistryName(color)).parent(type.getRegistryName(color));
        }
        this.cubeAll("tinted_glass", new ResourceLocation("connectedglass", "tinted_glass"));
        this.model("item/tinted_glass").parent("tinted_glass");

        // Pane item model template
        this.model("pane_item_template")
            .parent("minecraft", "block/block")
            .particleTexture("#all")
            .element(element ->
                element.shape(7, 0, 0, 9, 16, 16)
                    .allFaces((BiConsumer<EnumFacing,FaceBuilder>)(side, face) -> face.texture(side == EnumFacing.EAST || side == EnumFacing.WEST ? "#all" : "#edge"))
            );

        // Create the pane models
        for(CGGlassType type : CGGlassType.values()){
            if(type.hasPanes){
                this.model("item/" + type.getPaneRegistryName()).parent("pane_item_template")
                    .texture("all", type.getRegistryName() + "/" + type.getRegistryName())
                    .texture("edge", type.getRegistryName() + "/" + type.getRegistryName() + "_edge");
                for(EnumDyeColor color : EnumDyeColor.values())
                    this.model("item/" + type.getPaneRegistryName(color)).parent("pane_item_template")
                        .texture("all", type.getRegistryName() + "/" + type.getRegistryName(color))
                        .texture("edge", type.getRegistryName() + "/" + type.getRegistryName(color) + "_edge");
            }
        }
    }
}
