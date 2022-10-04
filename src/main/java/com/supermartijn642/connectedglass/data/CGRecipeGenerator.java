package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.*;
import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.registry.Registries;
import com.supermartijn642.core.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * Created 5/15/2020 by SuperMartijn642
 */
public class CGRecipeGenerator extends RecipeGenerator {

    private Block vanillaBlock;
    private final List<Pair<EnumDyeColor,Block>> vanillaBlocks = new ArrayList<>();
    private final EnumMap<EnumDyeColor,Block> vanillaColoredBlocks = new EnumMap<>(EnumDyeColor.class);
    private Block vanillaPane;
    private final List<Pair<EnumDyeColor,Block>> vanillaPanes = new ArrayList<>();
    private final EnumMap<EnumDyeColor,Block> vanillaColoredPanes = new EnumMap<>(EnumDyeColor.class);

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
                EnumDyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                Block previous = type.isTinted ?
                    lastTypeTinted == null ? color == null ? ConnectedGlass.tinted_glass : null : lastTypeTinted.getBlock(color) :
                    lastType == null ? this.getVanillaBlock(color) : lastType.getBlock(color);
                if(previous != null){
                    this.shaped(Registries.BLOCKS.getIdentifier(block).getResourcePath() + "1", Item.getItemFromBlock(block), 4)
                        .pattern("AA")
                        .pattern("AA")
                        .input('A', Item.getItemFromBlock(previous))
                        .unlockedBy(Item.getItemFromBlock(previous));
                }
            }

            // colored blocks from dyes
            for(CGColoredGlassBlock block : type.colored_blocks.values()){
                this.shaped(Registries.BLOCKS.getIdentifier(block).getResourcePath() + "2", Item.getItemFromBlock(block), 8)
                    .pattern("AAA")
                    .pattern("ABA")
                    .pattern("AAA")
                    .input('A', Item.getItemFromBlock(type.block))
                    .input('B', "dye" + Character.toUpperCase(block.getColor().getUnlocalizedName().charAt(0)) + block.getColor().getUnlocalizedName().substring(1))
                    .unlockedBy(Item.getItemFromBlock(type.block))
                    .unlockedByOreDict("dye" + Character.toUpperCase(block.getColor().getUnlocalizedName().charAt(0)) + block.getColor().getUnlocalizedName().substring(1));
            }

            if(type.hasPanes){
                // panes from previous type
                for(CGPaneBlock pane : type.panes){
                    EnumDyeColor color = pane instanceof CGColoredPaneBlock ? ((CGColoredPaneBlock)pane).getColor() : null;
                    Block previous = type.isTinted ?
                        lastTypeTinted == null ? null : lastTypeTinted.getPane(color) :
                        lastType == null ? this.getVanillaPane(color) : lastType.getPane(color);
                    if(previous != null){
                        this.shaped(Registries.BLOCKS.getIdentifier(pane).getResourcePath() + "1", Item.getItemFromBlock(pane), 4)
                            .pattern("AA")
                            .pattern("AA")
                            .input('A', Item.getItemFromBlock(previous))
                            .unlockedBy(Item.getItemFromBlock(previous));
                    }
                }

                // colored panes from dyes
                for(CGColoredPaneBlock pane : type.colored_panes.values()){
                    this.shaped(Registries.BLOCKS.getIdentifier(pane).getResourcePath() + "2", Item.getItemFromBlock(pane), 8)
                        .pattern("AAA")
                        .pattern("ABA")
                        .pattern("AAA")
                        .input('A', Item.getItemFromBlock(type.pane))
                        .input('B', "dye" + Character.toUpperCase(pane.getColor().getUnlocalizedName().charAt(0)) + pane.getColor().getUnlocalizedName().substring(1))
                        .unlockedBy(Item.getItemFromBlock(type.pane))
                        .unlockedByOreDict("dye" + Character.toUpperCase(pane.getColor().getUnlocalizedName().charAt(0)) + pane.getColor().getUnlocalizedName().substring(1));
                }

                // panes from blocks
                for(CGGlassBlock block : type.blocks){
                    EnumDyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).getColor() : null;
                    CGPaneBlock pane = type.getPane(color);
                    this.shaped(Registries.BLOCKS.getIdentifier(pane).getResourcePath() + "3", Item.getItemFromBlock(pane), 16)
                        .pattern("AAA")
                        .pattern("AAA")
                        .input('A', Item.getItemFromBlock(block))
                        .unlockedBy(Item.getItemFromBlock(block));
                }
            }

            if(type.isTinted)
                lastTypeTinted = type;
            else
                lastType = type;
        }

        // blocks from previous type to vanilla
        for(Pair<EnumDyeColor,Block> block : this.vanillaBlocks){
            EnumDyeColor color = block.left();
            Block previous = lastType.getBlock(color);
            String name = "vanilla_" + Registries.BLOCKS.getIdentifier(block.right()).getResourcePath();
            this.shaped(color == null ? name : name + "_" + color.getName(), Item.getItemFromBlock(block.right()), color == null ? 0 : color.getMetadata(), 4)
                .pattern("AA")
                .pattern("AA")
                .input('A', Item.getItemFromBlock(previous))
                .unlockedBy(Item.getItemFromBlock(previous));
        }

        // panes from previous type to vanilla
        for(Pair<EnumDyeColor,Block> pane : this.vanillaPanes){
            EnumDyeColor color = pane.left();
            Block previous = lastType.getPane(color);
            String name = "vanilla_" + Registries.BLOCKS.getIdentifier(pane.right()).getResourcePath();
            this.shaped(color == null ? name : name + "_" + color.getName(), Item.getItemFromBlock(pane.right()), color == null ? 0 : color.getMetadata(), 4)
                .pattern("AA")
                .pattern("AA")
                .input('A', Item.getItemFromBlock(previous))
                .unlockedBy(Item.getItemFromBlock(previous));
        }

        // blocks from previous type to vanilla tinted
        Block previous = lastTypeTinted.getBlock(null);
        this.shaped("vanilla_" + Registries.BLOCKS.getIdentifier(ConnectedGlass.tinted_glass).getResourcePath(), Item.getItemFromBlock(ConnectedGlass.tinted_glass), 4)
            .pattern("AA")
            .pattern("AA")
            .input('A', Item.getItemFromBlock(previous))
            .unlockedBy(Item.getItemFromBlock(previous));

        // regular tinted glass
        this.shaped(Item.getItemFromBlock(ConnectedGlass.tinted_glass))
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .input('A', "gemQuartz")
            .input('B', "dyeBlack")
            .input('C', "blockGlass")
            .unlockedByOreDict("blockGlass");
    }

    private void gatherVanillaBlocks(){
        this.addVanillaBlock(Blocks.GLASS, null);
        Arrays.stream(EnumDyeColor.values()).forEach(color -> this.addVanillaBlock(Blocks.STAINED_GLASS, color));
    }

    private void addVanillaBlock(Block block, EnumDyeColor color){
        this.vanillaBlocks.add(Pair.of(color, block));
        if(color == null)
            this.vanillaBlock = block;
        else
            this.vanillaColoredBlocks.put(color, block);
    }

    private void gatherVanillaPanes(){
        this.addVanillaPane(Blocks.GLASS_PANE, null);
        Arrays.stream(EnumDyeColor.values()).forEach(color -> this.addVanillaPane(Blocks.STAINED_GLASS_PANE, color));
    }

    private void addVanillaPane(Block pane, EnumDyeColor color){
        this.vanillaPanes.add(Pair.of(color, pane));
        if(color == null)
            this.vanillaPane = pane;
        else
            this.vanillaColoredPanes.put(color, pane);
    }

    private Block getVanillaBlock(EnumDyeColor color){
        if(color == null)
            return this.vanillaBlock;
        return this.vanillaColoredBlocks.get(color);
    }

    private Block getVanillaPane(EnumDyeColor color){
        if(color == null)
            return this.vanillaPane;
        return this.vanillaColoredPanes.get(color);
    }

    private static String getColorOreDict(EnumDyeColor color){
        if(color == EnumDyeColor.SILVER)
            return "dyeLightGray";
        String ore = color.getUnlocalizedName();
        return "dye" + Character.toUpperCase(ore.charAt(0)) + ore.substring(1);
    }
}
