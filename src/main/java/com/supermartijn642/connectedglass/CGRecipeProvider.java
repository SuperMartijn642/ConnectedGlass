package com.supermartijn642.connectedglass;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created 5/15/2020 by SuperMartijn642
 */
public class CGRecipeProvider extends RecipeProvider {

    private Block vanillaBlock;
    private final List<Block> vanillaBlocks = new ArrayList<>();
    private final EnumMap<DyeColor,Block> vanillaColoredBlocks = new EnumMap<>(DyeColor.class);
    private Block vanillaPane;
    private final List<Block> vanillaPanes = new ArrayList<>();
    private final EnumMap<DyeColor,Block> vanillaColoredPanes = new EnumMap<>(DyeColor.class);

    public CGRecipeProvider(DataGenerator generatorIn){
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer){
        this.gatherVanillaBlocks();
        this.gatherVanillaPanes();

        CGGlassType lastType = null;
        for(CGGlassType type : CGGlassType.values()){
            // blocks from previous type
            for(CGGlassBlock block : type.blocks){
                DyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                Block previous = lastType == null ? getVanillaBlock(color) : lastType.getBlock(color);
                ShapedRecipeBuilder.shapedRecipe(block, 4)
                    .patternLine("GG").patternLine("GG")
                    .key('G', previous)
                    .addCriterion("glass", InventoryChangeTrigger.Instance.forItems(previous))
                    .build(consumer, block.getRegistryName() + "1");
            }

            // colored blocks from dyes
            for(CGColoredGlassBlock block : type.colored_blocks.values()){
                ShapedRecipeBuilder.shapedRecipe(block, 8)
                    .patternLine("GGG").patternLine("GDG").patternLine("GGG")
                    .key('G', type.block)
                    .key('D', block.getColor().getTag())
                    .addCriterion("glass", InventoryChangeTrigger.Instance.forItems(type.block))
                    .addCriterion("dye", InventoryChangeTrigger.Instance.forItems(block.getColor().getTag().getAllElements().toArray(new Item[0])))
                    .build(consumer, block.getRegistryName() + "2");
            }

            // panes from previous type
            for(CGPaneBlock pane : type.panes){
                DyeColor color = pane instanceof CGColoredPaneBlock ? ((CGColoredPaneBlock)pane).getColor() : null;
                Block previous = lastType == null ? getVanillaPane(color) : lastType.getPane(color);
                ShapedRecipeBuilder.shapedRecipe(pane, 4)
                    .patternLine("GG").patternLine("GG")
                    .key('G', previous)
                    .addCriterion("glass_pane", InventoryChangeTrigger.Instance.forItems(previous))
                    .build(consumer, pane.getRegistryName() + "1");
            }

            // colored panes from dyes
            for(CGColoredPaneBlock pane : type.colored_panes.values()){
                ShapedRecipeBuilder.shapedRecipe(pane, 8)
                    .patternLine("GGG").patternLine("GDG").patternLine("GGG")
                    .key('G', type.pane)
                    .key('D', pane.getColor().getTag())
                    .addCriterion("glass_pane", InventoryChangeTrigger.Instance.forItems(type.pane))
                    .addCriterion("dye", InventoryChangeTrigger.Instance.forItems(pane.getColor().getTag().getAllElements().toArray(new Item[0])))
                    .build(consumer, pane.getRegistryName() + "2");
            }

            lastType = type;
        }

        // blocks from previous type
        for(Block block : this.vanillaBlocks){
            DyeColor color = block instanceof IBeaconBeamColorProvider ? ((IBeaconBeamColorProvider)block).getColor() : null;
            Block previous = lastType.getBlock(color);
            ShapedRecipeBuilder.shapedRecipe(block, 4)
                .patternLine("GG").patternLine("GG")
                .key('G', previous)
                .addCriterion("glass", InventoryChangeTrigger.Instance.forItems(previous))
                .build(consumer, new ResourceLocation("connectedglass", "vanilla_" + block.getRegistryName().getPath()));
        }

        // panes from previous type
        for(Block pane : this.vanillaPanes){
            DyeColor color = pane instanceof IBeaconBeamColorProvider ? ((IBeaconBeamColorProvider)pane).getColor() : null;
            Block previous = lastType.getPane(color);
            ShapedRecipeBuilder.shapedRecipe(pane, 4)
                .patternLine("GG").patternLine("GG")
                .key('G', previous)
                .addCriterion("glass_pane", InventoryChangeTrigger.Instance.forItems(previous))
                .build(consumer, new ResourceLocation("connectedglass", "vanilla_" + pane.getRegistryName().getPath()));
        }
    }

    private void gatherVanillaBlocks(){
        this.addVanillaBlock(Blocks.GLASS);
        this.addVanillaBlock(Blocks.WHITE_STAINED_GLASS);
        this.addVanillaBlock(Blocks.ORANGE_STAINED_GLASS);
        this.addVanillaBlock(Blocks.MAGENTA_STAINED_GLASS);
        this.addVanillaBlock(Blocks.LIGHT_BLUE_STAINED_GLASS);
        this.addVanillaBlock(Blocks.YELLOW_STAINED_GLASS);
        this.addVanillaBlock(Blocks.LIME_STAINED_GLASS);
        this.addVanillaBlock(Blocks.PINK_STAINED_GLASS);
        this.addVanillaBlock(Blocks.GRAY_STAINED_GLASS);
        this.addVanillaBlock(Blocks.LIGHT_GRAY_STAINED_GLASS);
        this.addVanillaBlock(Blocks.CYAN_STAINED_GLASS);
        this.addVanillaBlock(Blocks.PURPLE_STAINED_GLASS);
        this.addVanillaBlock(Blocks.BLUE_STAINED_GLASS);
        this.addVanillaBlock(Blocks.BROWN_STAINED_GLASS);
        this.addVanillaBlock(Blocks.GREEN_STAINED_GLASS);
        this.addVanillaBlock(Blocks.RED_STAINED_GLASS);
        this.addVanillaBlock(Blocks.BLACK_STAINED_GLASS);
    }

    private void addVanillaBlock(Block block){
        this.vanillaBlocks.add(block);
        DyeColor color = block instanceof IBeaconBeamColorProvider ? ((IBeaconBeamColorProvider)block).getColor() : null;
        if(color == null)
            this.vanillaBlock = block;
        else
            this.vanillaColoredBlocks.put(color, block);
    }

    private void gatherVanillaPanes(){
        this.addVanillaPane(Blocks.GLASS_PANE);
        this.addVanillaPane(Blocks.WHITE_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.ORANGE_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.MAGENTA_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.YELLOW_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.LIME_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.PINK_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.GRAY_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.CYAN_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.PURPLE_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.BLUE_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.BROWN_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.GREEN_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.RED_STAINED_GLASS_PANE);
        this.addVanillaPane(Blocks.BLACK_STAINED_GLASS_PANE);
    }

    private void addVanillaPane(Block pane){
        this.vanillaPanes.add(pane);
        DyeColor color = pane instanceof IBeaconBeamColorProvider ? ((IBeaconBeamColorProvider)pane).getColor() : null;
        if(color == null)
            this.vanillaPane = pane;
        else
            this.vanillaColoredPanes.put(color, pane);
    }

    private Block getVanillaBlock(DyeColor color){
        if(color == null)
            return this.vanillaBlock;
        return this.vanillaColoredBlocks.get(color);
    }

    private Block getVanillaPane(DyeColor color){
        if(color == null)
            return this.vanillaPane;
        return this.vanillaColoredPanes.get(color);
    }

    private List<Block> getVanillaBlocks(){
        return this.vanillaBlocks;
    }

    private List<Block> getVanillaPanes(){
        return this.vanillaPanes;
    }

    @Override
    public String getName(){
        return "connectedglass:recipes";
    }
}
