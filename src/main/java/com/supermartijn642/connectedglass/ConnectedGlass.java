package com.supermartijn642.connectedglass;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod("connectedglass")
public class ConnectedGlass {

    public static final List<CGGlassBlock> BLOCKS = new ArrayList<>();
    public static final List<CGPaneBlock> PANES = new ArrayList<>();

    public ConnectedGlass(){
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            // add blocks
            BLOCKS.add(new CGGlassBlock("borderless_glass", true));
            for(DyeColor color : DyeColor.values())
                BLOCKS.add(new CGColoredGlassBlock("borderless_glass_" + color.name().toLowerCase(Locale.ROOT), true, color));
            BLOCKS.add(new CGGlassBlock("clear_glass", true));
            for(DyeColor color : DyeColor.values())
                BLOCKS.add(new CGColoredGlassBlock("clear_glass_" + color.name().toLowerCase(Locale.ROOT), true, color));

            // add panes
            for(CGGlassBlock block : BLOCKS)
                PANES.add(block.createPane());

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
            e.getRegistry().register(new BlockItem(block, new Item.Properties().group(ItemGroup.SEARCH)).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }
}
