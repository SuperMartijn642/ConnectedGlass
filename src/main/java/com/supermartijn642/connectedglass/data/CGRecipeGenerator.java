package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.*;
import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Created 5/15/2020 by SuperMartijn642
 */
public class CGRecipeGenerator extends RecipeGenerator {

    private Block vanillaBlock;
    private final List<Block> vanillaBlocks = new ArrayList<>();
    private final EnumMap<DyeColor,Block> vanillaColoredBlocks = new EnumMap<>(DyeColor.class);
    private Block vanillaPane;
    private final List<Block> vanillaPanes = new ArrayList<>();
    private final EnumMap<DyeColor,Block> vanillaColoredPanes = new EnumMap<>(DyeColor.class);

    public CGRecipeGenerator(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    public void generate(){
        this.gatherVanillaBlocks();
        this.gatherVanillaPanes();

        CGGlassType lastType = null;
        CGGlassType lastTypeTinted = null;
        for(CGGlassType type : CGGlassType.values()){
            // blocks from previous type
            for(CGGlassBlock block : type.blocks){
                DyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                Block previous = type.isTinted ?
                    lastTypeTinted == null ? color == null ? Blocks.TINTED_GLASS : null : lastTypeTinted.getBlock(color) :
                    lastType == null ? this.getVanillaBlock(color) : lastType.getBlock(color);
                if(previous != null){
                    this.shaped(Registries.BLOCKS.getIdentifier(block).getPath() + "1", block, 4)
                        .pattern("AA")
                        .pattern("AA")
                        .input('A', previous)
                        .unlockedBy(previous);
                }
            }

            // colored blocks from dyes
            for(CGColoredGlassBlock block : type.colored_blocks.values()){
                this.shaped(Registries.BLOCKS.getIdentifier(block).getPath() + "2", block, 8)
                    .pattern("AAA")
                    .pattern("ABA")
                    .pattern("AAA")
                    .input('A', type.block)
                    .input('B', block.getColor().getTag())
                    .unlockedBy(type.block)
                    .unlockedBy(block.getColor().getTag());
            }

            if(type.hasPanes){
                // panes from previous type
                for(CGPaneBlock pane : type.panes){
                    DyeColor color = pane instanceof CGColoredPaneBlock ? ((CGColoredPaneBlock)pane).getColor() : null;
                    Block previous = type.isTinted ?
                        lastTypeTinted == null ? null : lastTypeTinted.getPane(color) :
                        lastType == null ? this.getVanillaPane(color) : lastType.getPane(color);
                    if(previous != null){
                        this.shaped(Registries.BLOCKS.getIdentifier(pane).getPath() + "1", pane, 4)
                            .pattern("AA")
                            .pattern("AA")
                            .input('A', previous)
                            .unlockedBy(previous);
                    }
                }

                // colored panes from dyes
                for(CGColoredPaneBlock pane : type.colored_panes.values()){
                    this.shaped(Registries.BLOCKS.getIdentifier(pane).getPath() + "2", pane, 8)
                        .pattern("AAA")
                        .pattern("ABA")
                        .pattern("AAA")
                        .input('A', type.pane)
                        .input('B', pane.getColor().getTag())
                        .unlockedBy(type.pane)
                        .unlockedBy(pane.getColor().getTag());
                }

                // panes from blocks
                for(CGGlassBlock block : type.blocks){
                    DyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                    CGPaneBlock pane = type.getPane(color);
                    this.shaped(Registries.BLOCKS.getIdentifier(pane).getPath() + "3", pane, 16)
                        .pattern("AAA")
                        .pattern("AAA")
                        .input('A', block)
                        .unlockedBy(block);
                }
            }

            if(type.isTinted)
                lastTypeTinted = type;
            else
                lastType = type;
        }

        // blocks from previous type to vanilla
        for(Block block : this.vanillaBlocks){
            DyeColor color = block instanceof BeaconBeamBlock ? ((BeaconBeamBlock)block).getColor() : null;
            Block previous = lastType.getBlock(color);
            this.shaped("vanilla_" + Registries.BLOCKS.getIdentifier(block).getPath(), block, 4)
                .pattern("AA")
                .pattern("AA")
                .input('A', previous)
                .unlockedBy(previous);
        }

        // panes from previous type to vanilla
        for(Block pane : this.vanillaPanes){
            DyeColor color = pane instanceof BeaconBeamBlock ? ((BeaconBeamBlock)pane).getColor() : null;
            Block previous = lastType.getPane(color);
            this.shaped("vanilla_" + Registries.BLOCKS.getIdentifier(pane).getPath(), pane, 4)
                .pattern("AA")
                .pattern("AA")
                .input('A', previous)
                .unlockedBy(previous);
        }

        // blocks from previous type to vanilla tinted
        Block previous = lastTypeTinted.getBlock(null);
        this.shaped("vanilla_" + Registries.BLOCKS.getIdentifier(Blocks.TINTED_GLASS).getPath(), Blocks.TINTED_GLASS, 4)
            .pattern("AA")
            .pattern("AA")
            .input('A', previous)
            .unlockedBy(previous);
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
        DyeColor color = block instanceof BeaconBeamBlock ? ((BeaconBeamBlock)block).getColor() : null;
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
        DyeColor color = pane instanceof BeaconBeamBlock ? ((BeaconBeamBlock)pane).getColor() : null;
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
}
