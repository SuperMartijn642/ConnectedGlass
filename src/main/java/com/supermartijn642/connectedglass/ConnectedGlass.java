package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.data.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod("connectedglass")
public class ConnectedGlass {

    public static final List<CGGlassBlock> BLOCKS = new ArrayList<>();
    public static final List<CGPaneBlock> PANES = new ArrayList<>();

    public static final CreativeModeTab GROUP = new CreativeModeTab("connectedglass") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(CGGlassType.BORDERLESS_GLASS.block);
        }
    };

    public ConnectedGlass(){
    }

    @Mod.EventBusSubscriber(modid = "connectedglass", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            // add blocks
            for(CGGlassType type : CGGlassType.values()){
                type.init();
                BLOCKS.addAll(type.blocks);
                if(type.hasPanes)
                    PANES.addAll(type.panes);
            }

            // register blocks
            for(Block block : BLOCKS)
                e.getRegistry().register(block);

            // register panes
            for(Block pane : PANES)
                e.getRegistry().register(pane);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            for(Block block : BLOCKS)
                registerItemBlock(e, block);
            for(Block pane : PANES)
                registerItemBlock(e, pane);
        }

        private static void registerItemBlock(RegistryEvent.Register<Item> e, Block block){
            e.getRegistry().register(new BlockItem(block, new Item.Properties().tab(GROUP)).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }

        @SubscribeEvent
        public static void registerDataProviders(final GatherDataEvent e){
            if(e.includeServer()){
                e.getGenerator().addProvider(new CGRecipeProvider(e.getGenerator()));
                CGTagProvider.init();
                CGBlockTagProvider blockTagProvider = new CGBlockTagProvider(e.getGenerator(), e.getExistingFileHelper());
                e.getGenerator().addProvider(blockTagProvider);
                e.getGenerator().addProvider(new CGItemTagProvider(e.getGenerator(), blockTagProvider, e.getExistingFileHelper()));
                e.getGenerator().addProvider(new CGLootTableProvider(e.getGenerator()));
            }

            if(e.includeClient()){
                e.getGenerator().addProvider(new CGDummyBlockStateProvider(e.getGenerator(), e.getExistingFileHelper()));
                e.getGenerator().addProvider(new CGDummyItemModelProvider(e.getGenerator(), e.getExistingFileHelper()));
            }
        }
    }

}
