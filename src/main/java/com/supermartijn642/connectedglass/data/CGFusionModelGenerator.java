package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.CGPaneBlock;
import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.data.ConnectingModelData;
import com.supermartijn642.fusion.api.predicate.ConnectionDirection;
import com.supermartijn642.fusion.api.predicate.ConnectionPredicate;
import com.supermartijn642.fusion.api.predicate.DefaultConnectionPredicates;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;
import com.supermartijn642.fusion.api.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class CGFusionModelGenerator extends FusionModelProvider {

    public CGFusionModelGenerator(DataGenerator generator){
        super("connectedglass", generator);
    }

    @Override
    public void generate(){
        // Create the glass block models
        for(CGGlassType type : CGGlassType.values()){
            this.addModel(new ResourceLocation("connectedglass", type.getRegistryName()),
                ModelInstance.of(
                    DefaultModelTypes.CONNECTING,
                    ConnectingModelData.builder()
                        .parent(new ResourceLocation("block/cube_all"))
                        .texture("all", new ResourceLocation("connectedglass", type.getRegistryName() + "/" + type.getRegistryName()))
                        .build()
                )
            );
            for(DyeColor color : DyeColor.values()){
                this.addModel(new ResourceLocation("connectedglass", type.getRegistryName(color)),
                    ModelInstance.of(
                        DefaultModelTypes.CONNECTING,
                        ConnectingModelData.builder()
                            .parent(new ResourceLocation("block/cube_all"))
                            .texture("all", new ResourceLocation("connectedglass", type.getRegistryName() + "/" + type.getRegistryName(color)))
                            .build()
                    )
                );
            }
        }

        // Create the pane models
        for(CGGlassType type : CGGlassType.values()){
            if(type.hasPanes){
                this.createPaneModels(type.getPaneRegistryName(), type.getRegistryName() + "/" + type.getRegistryName(), type.getPane());
                for(DyeColor color : DyeColor.values())
                    this.createPaneModels(type.getPaneRegistryName(color), type.getRegistryName() + "/" + type.getRegistryName(color), type.getPane(color));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void createPaneModels(String identifier, String texture, CGPaneBlock block){
        this.addModel(new ResourceLocation("connectedglass", identifier + "_post"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_post"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.isSameBlock())
                    .build()
            )
        );
        ConnectionPredicate notUpOrDown = DefaultConnectionPredicates.isDirection(Arrays.stream(ConnectionDirection.values()).filter(dir -> dir != ConnectionDirection.TOP && dir != ConnectionDirection.BOTTOM).toArray(ConnectionDirection[]::new));
        this.addModel(new ResourceLocation("connectedglass", identifier + "_side_north"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_side"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.matchState(block, Pair.of(CGPaneBlock.NORTH, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(new ResourceLocation("connectedglass", identifier + "_side_east"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_side"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.matchState(block, Pair.of(CGPaneBlock.EAST, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(new ResourceLocation("connectedglass", identifier + "_side_south"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_side_alt"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.matchState(block, Pair.of(CGPaneBlock.SOUTH, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(new ResourceLocation("connectedglass", identifier + "_side_west"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_side_alt"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.matchState(block, Pair.of(CGPaneBlock.WEST, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(new ResourceLocation("connectedglass", identifier + "_noside"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_noside"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.isSameBlock())
                    .build()
            )
        );
        this.addModel(new ResourceLocation("connectedglass", identifier + "_noside_alt"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(new ResourceLocation("connectedglass", "block/template_glass_pane_noside_alt"))
                    .texture("pane", new ResourceLocation("connectedglass", texture))
                    .texture("edge", new ResourceLocation("connectedglass", texture + "_edge"))
                    .connection(DefaultConnectionPredicates.isSameBlock())
                    .build()
            )
        );
    }
}
