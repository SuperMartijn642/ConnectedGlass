package com.supermartijn642.connectedglass;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod(modid = ConnectedGlass.MODID, name = ConnectedGlass.NAME, version = ConnectedGlass.VERSION, dependencies = ConnectedGlass.DEPENDENCIES)
public class ConnectedGlass {

    public static final String MODID = "connectedglass";
    public static final String NAME = "Connected Glass";
    public static final String VERSION = "1.0.8";
    public static final String DEPENDENCIES = "";

    public static final List<CGGlassBlock> BLOCKS = new ArrayList<>();
    public static final List<CGPaneBlock> PANES = new ArrayList<>();

    public ConnectedGlass(){
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        // register to ore dict
        for(CGGlassType type : CGGlassType.values()){
            type.blocks.forEach(block -> {
                OreDictionary.registerOre("blockGlass", block);
                OreDictionary.registerOre("blockGlass", Item.getItemFromBlock(block));
            });
            OreDictionary.registerOre("blockGlassColorless", type.block);
            OreDictionary.registerOre("blockGlassColorless", Item.getItemFromBlock(type.block));
            type.colored_blocks.forEach((color, block) -> {
                String name = color == EnumDyeColor.SILVER ? "LightGray" : color.getUnlocalizedName().toUpperCase().charAt(0) + color.getUnlocalizedName().substring(1);
                OreDictionary.registerOre("blockGlass" + name, block);
                OreDictionary.registerOre("blockGlass" + name, Item.getItemFromBlock(block));
                OreDictionary.registerOre("blockGlassColored", block);
                OreDictionary.registerOre("blockGlassColored", Item.getItemFromBlock(block));
            });

            type.panes.forEach(pane -> {
                OreDictionary.registerOre("blockPane", pane);
                OreDictionary.registerOre("blockPane", Item.getItemFromBlock(pane));
            });
            OreDictionary.registerOre("blockPaneColorless", type.pane);
            OreDictionary.registerOre("blockPaneColorless", Item.getItemFromBlock(type.pane));
            type.colored_panes.forEach((color, pane) -> {
                String name = color == EnumDyeColor.SILVER ? "LightGray" : color.getUnlocalizedName().toUpperCase().charAt(0) + color.getUnlocalizedName().substring(1);
                OreDictionary.registerOre("blockPane" + name, pane);
                OreDictionary.registerOre("blockPane" + name, Item.getItemFromBlock(pane));
                OreDictionary.registerOre("blockPaneColored", pane);
                OreDictionary.registerOre("blockPaneColored", Item.getItemFromBlock(pane));
            });
        }
    }

    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            // add blocks
            for(CGGlassType type : CGGlassType.values()){
                type.init();
                BLOCKS.addAll(type.blocks);
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
            e.getRegistry().register(new ItemBlock(block).setCreativeTab(CreativeTabs.SEARCH).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }

        @SubscribeEvent
        public static void onRecipeRegistry(final RegistryEvent.Register<IRecipe> e){
            CGRecipeProvider.registerRecipes(e);
        }
    }

}
