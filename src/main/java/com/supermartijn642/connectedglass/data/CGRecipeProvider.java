package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

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
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer){
        this.gatherVanillaBlocks();
        this.gatherVanillaPanes();

        CGGlassType lastType = null;
        CGGlassType lastTypeTinted = null;
        for(CGGlassType type : CGGlassType.values()){
            // blocks from previous type
            for(CGGlassBlock block : type.blocks){
                DyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                Block previous = type.isTinted ?
                    lastTypeTinted == null ? color == null ? ConnectedGlass.tinted_glass : null : lastTypeTinted.getBlock(color) :
                    lastType == null ? this.getVanillaBlock(color) : lastType.getBlock(color);
                if(previous != null){
                    ShapedRecipeBuilder.shaped(block, 4)
                        .pattern("GG").pattern("GG")
                        .define('G', previous)
                        .unlocks("glass", this.has(previous))
                        .save(consumer, block.getRegistryName() + "1");
                }
            }

            // colored blocks from dyes
            for(CGColoredGlassBlock block : type.colored_blocks.values()){
                ShapedRecipeBuilder.shaped(block, 8)
                    .pattern("GGG").pattern("GDG").pattern("GGG")
                    .define('G', type.block)
                    .define('D', block.getColor().getTag())
                    .unlocks("glass", this.has(type.block))
                    .unlocks("dye", this.has(block.getColor().getTag()))
                    .save(consumer, block.getRegistryName() + "2");
            }

            if(type.hasPanes){
                // panes from previous type
                for(CGPaneBlock pane : type.panes){
                    DyeColor color = pane instanceof CGColoredPaneBlock ? ((CGColoredPaneBlock)pane).getColor() : null;
                    Block previous = type.isTinted ?
                        lastTypeTinted == null ? null : lastTypeTinted.getPane(color) :
                        lastType == null ? this.getVanillaPane(color) : lastType.getPane(color);
                    if(previous != null){
                        ShapedRecipeBuilder.shaped(pane, 4)
                            .pattern("GG").pattern("GG")
                            .define('G', previous)
                            .unlocks("glass_pane", this.has(previous))
                            .save(consumer, pane.getRegistryName() + "1");
                    }
                }

                // colored panes from dyes
                for(CGColoredPaneBlock pane : type.colored_panes.values()){
                    ShapedRecipeBuilder.shaped(pane, 8)
                        .pattern("GGG").pattern("GDG").pattern("GGG")
                        .define('G', type.pane)
                        .define('D', pane.getColor().getTag())
                        .unlocks("glass_pane", this.has(type.pane))
                        .unlocks("dye", this.has(pane.getColor().getTag()))
                        .save(consumer, pane.getRegistryName() + "2");
                }

                // panes from blocks
                for(CGGlassBlock block : type.blocks){
                    DyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                    CGPaneBlock pane = type.getPane(color);
                    ShapedRecipeBuilder.shaped(pane, 16)
                        .pattern("GGG").pattern("GGG")
                        .define('G', block)
                        .unlocks("glass", this.has(block))
                        .save(consumer, pane.getRegistryName() + "3");
                }
            }

            if(type.isTinted)
                lastTypeTinted = type;
            else
                lastType = type;
        }

        // blocks from previous type to vanilla
        for(Block block : this.vanillaBlocks){
            DyeColor color = block instanceof IBeaconBeamColorProvider ? ((IBeaconBeamColorProvider)block).getColor() : null;
            Block previous = lastType.getBlock(color);
            ShapedRecipeBuilder.shaped(block, 4)
                .pattern("GG").pattern("GG")
                .define('G', previous)
                .unlocks("glass", this.has(previous))
                .save(consumer, new ResourceLocation("connectedglass", "vanilla_" + block.getRegistryName().getPath()));
        }

        // panes from previous type to vanilla
        for(Block pane : this.vanillaPanes){
            DyeColor color = pane instanceof IBeaconBeamColorProvider ? ((IBeaconBeamColorProvider)pane).getColor() : null;
            Block previous = lastType.getPane(color);
            ShapedRecipeBuilder.shaped(pane, 4)
                .pattern("GG").pattern("GG")
                .define('G', previous)
                .unlocks("glass_pane", this.has(previous))
                .save(consumer, new ResourceLocation("connectedglass", "vanilla_" + pane.getRegistryName().getPath()));
        }

        // blocks from previous type to vanilla tinted
        Block previous = lastTypeTinted.getBlock(null);
        ShapedRecipeBuilder.shaped(ConnectedGlass.tinted_glass, 4)
            .pattern("GG").pattern("GG")
            .define('G', previous)
            .unlocks("glass", this.has(previous))
            .save(consumer, new ResourceLocation("connectedglass", "vanilla_" + ConnectedGlass.tinted_glass.getRegistryName().getPath()));

        // regular tinted glass
        ShapedRecipeBuilder.shaped(ConnectedGlass.tinted_glass)
            .pattern(" A ").pattern("BCB").pattern(" A ")
            .define('A', Tags.Items.GEMS_QUARTZ)
            .define('B', Tags.Items.DYES_BLACK)
            .define('C', Tags.Items.GLASS)
            .unlocks("glass", this.has(Tags.Items.GLASS))
            .save(consumer, new ResourceLocation("connectedglass", "tinted_glass"));
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
