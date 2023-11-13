package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;

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
            for(DyeColor color : DyeColor.values())
                this.model("item/" + type.getRegistryName(color)).parent(type.getRegistryName(color));
        }

        // Pane item model template
        this.model("pane_item_template")
            .parent("minecraft", "block/block")
            .particleTexture("#all")
            .element(element ->
                element.shape(7, 0, 0, 9, 16, 16)
                    .allFaces((BiConsumer<Direction,FaceBuilder>)(side, face) -> face.texture(side == Direction.EAST || side == Direction.WEST ? "#all" : "#edge"))
            );

        // Create the pane models
        for(CGGlassType type : CGGlassType.values()){
            if(type.hasPanes){
                this.model("item/" + type.getPaneRegistryName()).parent("pane_item_template")
                    .texture("all", type.getRegistryName() + "/" + type.getRegistryName())
                    .texture("edge", type.getRegistryName() + "/" + type.getRegistryName() + "_edge");
                for(DyeColor color : DyeColor.values())
                    this.model("item/" + type.getPaneRegistryName(color)).parent("pane_item_template")
                        .texture("all", type.getRegistryName() + "/" + type.getRegistryName(color))
                        .texture("edge", type.getRegistryName() + "/" + type.getRegistryName(color) + "_edge");
            }
        }
    }
}
