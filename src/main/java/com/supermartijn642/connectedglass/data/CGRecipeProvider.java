package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;

/**
 * Created 5/15/2020 by SuperMartijn642
 */
public class CGRecipeProvider {

    private static ItemStack vanillaBlock;
    private static final List<ItemStack> vanillaBlocks = new ArrayList<>();
    private static final EnumMap<EnumDyeColor,ItemStack> vanillaColoredBlocks = new EnumMap<>(EnumDyeColor.class);
    private static ItemStack vanillaPane;
    private static final List<ItemStack> vanillaPanes = new ArrayList<>();
    private static final EnumMap<EnumDyeColor,ItemStack> vanillaColoredPanes = new EnumMap<>(EnumDyeColor.class);

    public static void registerRecipes(RegistryEvent.Register<IRecipe> e){
        gatherVanillaBlocks();
        gatherVanillaPanes();

        CGGlassType lastType = null;
        CGGlassType lastTypeTinted = null;
        for(CGGlassType type : CGGlassType.values()){
            // blocks from previous type
            for(CGGlassBlock block : type.blocks){
                EnumDyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).color : null;
                ItemStack previous = type.isTinted ?
                    lastTypeTinted == null ? color == null ? new ItemStack(ConnectedGlass.tinted_glass) : null : new ItemStack(lastTypeTinted.getBlock(color)) :
                    lastType == null ? getVanillaBlock(color) : new ItemStack(lastType.getBlock(color));
                if(previous != null){
                    ShapedRecipes recipe = createShaped(new ResourceLocation(block.getRegistryName().getResourceDomain(), block.getRegistryName().getResourcePath() + "1"),
                        new ItemStack(block, 4), "GG", "GG", 'G', previous);
                    e.getRegistry().register(recipe);
                }
            }

            // colored blocks from dyes
            for(CGColoredGlassBlock block : type.colored_blocks.values()){
                ShapedRecipes recipe = createShaped(new ResourceLocation(block.getRegistryName().getResourceDomain(), block.getRegistryName().getResourcePath() + "2"),
                    new ItemStack(block, 8), "GGG", "GDG", "GGG", 'G', new ItemStack(type.block), 'D', getColorIngredient(block.color));
                e.getRegistry().register(recipe);
            }

            if(type.hasPanes){
                // panes from previous type
                for(CGPaneBlock pane : type.panes){
                    EnumDyeColor color = pane instanceof CGColoredPaneBlock ? ((CGColoredGlassBlock)pane.block).color : null;
                    ItemStack previous = type.isTinted ?
                        lastTypeTinted == null ? null : new ItemStack(lastTypeTinted.getPane(color)) :
                        lastType == null ? getVanillaPane(color) : new ItemStack(lastType.getPane(color));
                    if(previous != null){
                        ShapedRecipes recipe = createShaped(new ResourceLocation(pane.getRegistryName().getResourceDomain(), pane.getRegistryName().getResourcePath() + "1"),
                            new ItemStack(pane, 4), "GG", "GG", 'G', previous);
                        e.getRegistry().register(recipe);
                    }
                }

                // colored panes from dyes
                for(CGColoredPaneBlock pane : type.colored_panes.values()){
                    ShapedRecipes recipe = createShaped(new ResourceLocation(pane.getRegistryName().getResourceDomain(), pane.getRegistryName().getResourcePath() + "2"),
                        new ItemStack(pane, 8), "GGG", "GDG", "GGG", 'G', new ItemStack(type.pane), 'D', getColorIngredient(((CGColoredGlassBlock)pane.block).color));
                    e.getRegistry().register(recipe);
                }

                // panes from blocks
                for(CGGlassBlock block : type.blocks){
                    EnumDyeColor color = block instanceof CGColoredGlassBlock ? ((CGColoredGlassBlock)block).color : null;
                    CGPaneBlock pane = type.getPane(color);
                    ShapedRecipes recipe = createShaped(new ResourceLocation(pane.getRegistryName().getResourceDomain(), pane.getRegistryName().getResourcePath() + "3"),
                        new ItemStack(pane, 16), "GGG", "GGG", 'G', new ItemStack(block));
                    e.getRegistry().register(recipe);
                }
            }

            if(type.isTinted)
                lastTypeTinted = type;
            else
                lastType = type;
        }

        // blocks from previous type to vanilla
        for(ItemStack stack : vanillaBlocks){
            EnumDyeColor color = ((ItemBlock)stack.getItem()).getBlock() instanceof BlockStainedGlass ? EnumDyeColor.byMetadata(stack.getMetadata()) : null;
            Block previous = lastType.getBlock(color);
            ItemStack result = stack.copy();
            result.setCount(4);
            ShapedRecipes recipe = createShaped(new ResourceLocation("connectedglass", "vanilla_" + stack.getItem().getRegistryName().getResourcePath()),
                result, "GG", "GG", 'G', new ItemStack(previous));
            e.getRegistry().register(recipe);
        }

        // panes from previous type to vanilla
        for(ItemStack stack : vanillaPanes){
            EnumDyeColor color = ((ItemBlock)stack.getItem()).getBlock() instanceof BlockStainedGlassPane ? EnumDyeColor.byMetadata(stack.getMetadata()) : null;
            ItemStack previous = new ItemStack(lastType.getPane(color));
            ItemStack result = stack.copy();
            result.setCount(4);
            ShapedRecipes recipe = createShaped(new ResourceLocation("connectedglass", "vanilla_" + stack.getItem().getRegistryName().getResourcePath()),
                result, "GG", "GG", 'G', previous);
            e.getRegistry().register(recipe);
        }

        // blocks from previous type to vanilla tinted
        Block previous = lastTypeTinted.getBlock(null);
        ItemStack result = new ItemStack(ConnectedGlass.tinted_glass, 4);
        ShapedRecipes recipe = createShaped(new ResourceLocation("connectedglass", "vanilla_" + result.getItem().getRegistryName().getResourcePath()),
            result, "GG", "GG", 'G', new ItemStack(previous));
        e.getRegistry().register(recipe);

        // regular tinted glass
        recipe = createShaped(new ResourceLocation("connectedglass", "vanilla_" + ConnectedGlass.tinted_glass.getRegistryName().getResourcePath()),
            new ItemStack(ConnectedGlass.tinted_glass),
            " A ", "BCB", " A ",
            'A', new OreIngredient("gemQuartz"),
            'B', new OreIngredient("dyeBlack"),
            'C', new OreIngredient("blockGlass"));
        e.getRegistry().register(recipe);
    }

    private static ShapedRecipes createShaped(ResourceLocation registryName, ItemStack output, Object... params){
        CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(params);
        ShapedRecipes recipe = new ShapedRecipes("", primer.width, primer.height, primer.input, output);
        recipe.setRegistryName(registryName);
        return recipe;
    }

    private static void gatherVanillaBlocks(){
        addVanillaBlock(new ItemStack(Blocks.GLASS));
        for(EnumDyeColor color : EnumDyeColor.values())
            addVanillaBlock(new ItemStack(Blocks.STAINED_GLASS, 1, color.getMetadata()));
    }

    private static void addVanillaBlock(ItemStack stack){
        vanillaBlocks.add(stack);
        EnumDyeColor color = ((ItemBlock)stack.getItem()).getBlock() instanceof BlockStainedGlass ? EnumDyeColor.byMetadata(stack.getMetadata()) : null;
        if(color == null)
            vanillaBlock = stack;
        else
            vanillaColoredBlocks.put(color, stack);
    }

    private static void gatherVanillaPanes(){
        addVanillaPane(new ItemStack(Blocks.GLASS_PANE));
        for(EnumDyeColor color : EnumDyeColor.values())
            addVanillaPane(new ItemStack(Blocks.STAINED_GLASS_PANE, 1, color.getMetadata()));
    }

    private static void addVanillaPane(ItemStack stack){
        vanillaPanes.add(stack);
        EnumDyeColor color = ((ItemBlock)stack.getItem()).getBlock() instanceof BlockStainedGlassPane ? EnumDyeColor.byMetadata(stack.getMetadata()) : null;
        if(color == null)
            vanillaPane = stack;
        else
            vanillaColoredPanes.put(color, stack);
    }

    private static ItemStack getVanillaBlock(EnumDyeColor color){
        if(color == null)
            return vanillaBlock;
        return vanillaColoredBlocks.get(color);
    }

    private static ItemStack getVanillaPane(EnumDyeColor color){
        if(color == null)
            return vanillaPane;
        return vanillaColoredPanes.get(color);
    }

    private static OreIngredient getColorIngredient(EnumDyeColor color){
        if(color == EnumDyeColor.SILVER)
            return new OreIngredient("dyeLightGray");
        String ore = color.getUnlocalizedName();
        ore = "dye" + ore.substring(0, 1).toUpperCase(Locale.ROOT) + ore.substring(1);
        return new OreIngredient(ore);
    }
}
