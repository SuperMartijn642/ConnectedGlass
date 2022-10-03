package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.ConnectedGlass;
import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BlockStateProperties;

/**
 * Created 28/09/2022 by SuperMartijn642
 */
public class CGBlockStateGenerator extends BlockStateGenerator {

    public CGBlockStateGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        // Glass blocks
        for(CGGlassType type : CGGlassType.values()){
            this.blockState(type.getBlock()).emptyVariant(builder -> builder.model(type.getRegistryName()));
            for(DyeColor color : DyeColor.values())
                this.blockState(type.getBlock(color)).emptyVariant(builder -> builder.model(type.getRegistryName(color)));
        }
        this.blockState(ConnectedGlass.tinted_glass).emptyVariant(builder -> builder.model("tinted_glass"));

        // Panes
        for(CGGlassType type : CGGlassType.values()){
            if(!type.hasPanes)
                continue;
            String identifier = type.getPaneRegistryName();
            this.createPaneBlockState(identifier, type.getPane());
            for(DyeColor color : DyeColor.values()){
                identifier = type.getPaneRegistryName(color);
                this.createPaneBlockState(identifier, type.getPane(color));
            }
        }
    }

    private void createPaneBlockState(String identifier, Block pane){
        this.blockState(pane)
            .unconditionalMultipart(variant -> variant.model(identifier + "_post"))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.NORTH, true), variant -> variant.model(identifier + "_side"))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.EAST, true), variant -> variant.model(identifier + "_side", 0, 90))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.SOUTH, true), variant -> variant.model(identifier + "_side_alt"))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.WEST, true), variant -> variant.model(identifier + "_side_alt", 0, 90))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.NORTH, false), variant -> variant.model(identifier + "_noside"))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.EAST, false), variant -> variant.model(identifier + "_noside_alt"))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.SOUTH, false), variant -> variant.model(identifier + "_noside_alt", 0, 90))
            .multipart(condition -> condition.requireProperty(BlockStateProperties.WEST, false), variant -> variant.model(identifier + "_noside", 0, 270));
    }
}
