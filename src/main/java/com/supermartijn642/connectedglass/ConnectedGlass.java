package com.supermartijn642.connectedglass;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod(modid = ConnectedGlass.MODID, name = ConnectedGlass.NAME, version = ConnectedGlass.VERSION, dependencies = ConnectedGlass.DEPENDENCIES)
public class ConnectedGlass {

    public static final String MODID = "connectedglass";
    public static final String NAME = "Connected Glass";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "required-after:forge";

    public static final List<CGGlassBlock> BLOCKS = new ArrayList<>();
    public static final List<CGPaneBlock> PANES = new ArrayList<>();

    public ConnectedGlass(){
    }

    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            // add blocks
            BLOCKS.add(new CGGlassBlock("borderless_glass", true));
            for(EnumDyeColor color : EnumDyeColor.values())
                BLOCKS.add(new CGColoredGlassBlock("borderless_glass_" + color.name().toLowerCase(Locale.ROOT), true, color));
            BLOCKS.add(new CGGlassBlock("clear_glass", true));
            for(EnumDyeColor color : EnumDyeColor.values())
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
            e.getRegistry().register(new ItemBlock(block).setCreativeTab(CreativeTabs.SEARCH).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

}
