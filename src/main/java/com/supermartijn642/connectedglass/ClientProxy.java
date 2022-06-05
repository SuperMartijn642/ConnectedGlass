package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.model.CGBakedModel;
import com.supermartijn642.connectedglass.model.CGConnectedBakedModel;
import com.supermartijn642.connectedglass.model.CGConnectedPaneBakedModel;
import com.supermartijn642.connectedglass.model.CGPaneBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    public static final Map<CGGlassBlock,TextureAtlasSprite> TEXTURES = new HashMap<>();

    @SubscribeEvent
    public static void onSetup(FMLClientSetupEvent e){
        for(CGGlassBlock block : ConnectedGlass.BLOCKS)
            RenderTypeLookup.setRenderLayer(block, block instanceof CGColoredGlassBlock || block instanceof CGTintedGlassBlock ? RenderType.translucent() : RenderType.cutout());
        for(CGPaneBlock pane : ConnectedGlass.PANES)
            RenderTypeLookup.setRenderLayer(pane, pane instanceof CGColoredPaneBlock ? RenderType.translucent() : RenderType.cutoutMipped());

        RenderTypeLookup.setRenderLayer(ConnectedGlass.tinted_glass, RenderType.translucent());
    }

    @SubscribeEvent
    public static void onBake(ModelBakeEvent e){
        for(CGGlassBlock block : ConnectedGlass.BLOCKS){
            CGBakedModel model = block.connected ? new CGConnectedBakedModel(block) : new CGBakedModel(block);
            e.getModelRegistry().put(new ModelResourceLocation(block.getRegistryName(), ""), model);
            e.getModelRegistry().put(new ModelResourceLocation(block.getRegistryName(), "inventory"), model);
        }
        for(CGPaneBlock pane : ConnectedGlass.PANES){
            CGPaneBakedModel model = pane.block.connected ? new CGConnectedPaneBakedModel(pane) : new CGPaneBakedModel(pane);
            e.getModelRegistry().put(new ModelResourceLocation(pane.getRegistryName(), "inventory"), model);
            pane.getStateDefinition().getPossibleStates().forEach(state -> {
                String variant = state.toString();
                variant = variant.indexOf('[') > 0 ? variant.substring(variant.indexOf('[') + 1, variant.length() - 1) : "";
                e.getModelRegistry().put(new ModelResourceLocation(pane.getRegistryName(), variant), model);
            });
        }

        CGBakedModel model = new CGBakedModel(ConnectedGlass.tinted_glass);
        e.getModelRegistry().put(new ModelResourceLocation(ConnectedGlass.tinted_glass.getRegistryName(), ""), model);
        e.getModelRegistry().put(new ModelResourceLocation(ConnectedGlass.tinted_glass.getRegistryName(), "inventory"), model);
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre e){
        if(e.getMap().location().toString().equals("minecraft:textures/atlas/blocks.png")){
            for(CGGlassBlock block : ConnectedGlass.BLOCKS){
                e.addSprite(block.getRegistryName());
            }
            e.addSprite(ConnectedGlass.tinted_glass.getRegistryName());
        }
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Post e){
        if(e.getMap().location().toString().equals("minecraft:textures/atlas/blocks.png")){
            for(CGGlassBlock block : ConnectedGlass.BLOCKS){
                TEXTURES.put(block, e.getMap().getSprite(block.getRegistryName()));
            }
            TEXTURES.put(ConnectedGlass.tinted_glass, e.getMap().getSprite(ConnectedGlass.tinted_glass.getRegistryName()));
        }
    }

}
