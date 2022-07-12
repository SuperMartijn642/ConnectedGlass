package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.data.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

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
        public static void onRegisterEvent(RegisterEvent e){
            if(e.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS))
                onBlockRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if(e.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS))
                onItemRegistry(Objects.requireNonNull(e.getForgeRegistry()));
        }

        public static void onBlockRegistry(IForgeRegistry<Block> registry){
            // add blocks
            for(CGGlassType type : CGGlassType.values()){
                type.init();
                BLOCKS.addAll(type.blocks);
                if(type.hasPanes)
                    PANES.addAll(type.panes);
            }

            // register blocks
            for(CGGlassBlock block : BLOCKS)
                registry.register(block.getRegistryName(), block);

            // register panes
            for(CGPaneBlock pane : PANES)
                registry.register(pane.getRegistryName(), pane);
        }

        public static void onItemRegistry(IForgeRegistry<Item> registry){
            for(CGGlassBlock block : BLOCKS)
                registerItemBlock(registry, block.getRegistryName(), block);
            for(CGPaneBlock pane : PANES)
                registerItemBlock(registry, pane.getRegistryName(), pane);
        }

        private static void registerItemBlock(IForgeRegistry<Item> registry, ResourceLocation registryName, Block block){
            registry.register(registryName, new BlockItem(block, new Item.Properties().tab(GROUP)));
        }

        @SubscribeEvent
        public static void registerDataProviders(final GatherDataEvent e){
            if(e.includeServer()){
                e.getGenerator().addProvider(true, new CGRecipeProvider(e.getGenerator()));
                CGTagProvider.init();
                CGBlockTagProvider blockTagProvider = new CGBlockTagProvider(e.getGenerator(), e.getExistingFileHelper());
                e.getGenerator().addProvider(true, blockTagProvider);
                e.getGenerator().addProvider(true, new CGItemTagProvider(e.getGenerator(), blockTagProvider, e.getExistingFileHelper()));
                e.getGenerator().addProvider(true, new CGLootTableProvider(e.getGenerator()));
                e.getGenerator().addProvider(true, new CGChiselingRecipeProvider(e.getGenerator(), e.getExistingFileHelper()));
            }

            if(e.includeClient()){
                e.getGenerator().addProvider(true, new CGDummyBlockStateProvider(e.getGenerator(), e.getExistingFileHelper()));
                e.getGenerator().addProvider(true, new CGDummyItemModelProvider(e.getGenerator(), e.getExistingFileHelper()));
            }
        }
    }
}
