package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.CGPaneBlock;
import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.types.connecting.ConnectingModelData;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;
import com.supermartijn642.fusion.api.texture.types.connecting.predicates.ConnectionDirection;
import com.supermartijn642.fusion.api.texture.types.connecting.predicates.ConnectionPredicate;
import com.supermartijn642.fusion.api.texture.types.connecting.predicates.DefaultConnectionPredicates;
import com.supermartijn642.fusion.api.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.Arrays;
import java.util.List;

/**
 * Created 26/09/2022 by SuperMartijn642
 */
public class CGFusionModelGenerator extends FusionModelProvider {

    public CGFusionModelGenerator(DataGenerator generator){
        super("connectedglass", generator.getPackOutput());
    }

    @Override
    public void generate(){
        // Create the glass block models
        for(CGGlassType type : CGGlassType.values()){
            this.addModel(Identifier.fromNamespaceAndPath("connectedglass", type.getRegistryName()),
                ModelInstance.of(
                    DefaultModelTypes.CONNECTING,
                    ConnectingModelData.builder()
                        .parent(Identifier.withDefaultNamespace("block/cube_all"))
                        .material("all", Identifier.fromNamespaceAndPath("connectedglass", type.getRegistryName() + "/" + type.getRegistryName()))
                        .build()
                )
            );
            for(DyeColor color : DyeColor.values()){
                this.addModel(Identifier.fromNamespaceAndPath("connectedglass", type.getRegistryName(color)),
                    ModelInstance.of(
                        DefaultModelTypes.CONNECTING,
                        ConnectingModelData.builder()
                            .parent(Identifier.withDefaultNamespace("block/cube_all"))
                            .material("all", Identifier.fromNamespaceAndPath("connectedglass", type.getRegistryName() + "/" + type.getRegistryName(color)))
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
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_post"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_post"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.isSameBlock())
                    .build()
            )
        );
        ConnectionPredicate notUpOrDown = DefaultConnectionPredicates.isDirection(Arrays.stream(ConnectionDirection.values()).filter(dir -> dir != ConnectionDirection.TOP && dir != ConnectionDirection.BOTTOM).toArray(ConnectionDirection[]::new));
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_side_north"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_side"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.matchState(List.of(block), Pair.of(CGPaneBlock.NORTH, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_side_east"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_side"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.matchState(List.of(block), Pair.of(CGPaneBlock.EAST, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_side_south"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_side_alt"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.matchState(List.of(block), Pair.of(CGPaneBlock.SOUTH, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_side_west"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_side_alt"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.matchState(List.of(block), Pair.of(CGPaneBlock.WEST, true)).or(notUpOrDown))
                    .build()
            )
        );
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_noside"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_noside"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.isSameBlock())
                    .build()
            )
        );
        this.addModel(Identifier.fromNamespaceAndPath("connectedglass", identifier + "_noside_alt"),
            ModelInstance.of(
                DefaultModelTypes.CONNECTING,
                ConnectingModelData.builder()
                    .parent(Identifier.fromNamespaceAndPath("connectedglass", "block/template_glass_pane_noside_alt"))
                    .material("pane", Identifier.fromNamespaceAndPath("connectedglass", texture))
                    .material("edge", Identifier.fromNamespaceAndPath("connectedglass", texture + "_edge"))
                    .defaultConnections(DefaultConnectionPredicates.isSameBlock())
                    .build()
            )
        );
    }
}
