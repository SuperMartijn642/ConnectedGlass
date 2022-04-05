package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.ConnectedGlass;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Created 05/04/2022 by SuperMartijn642
 */
public class CGChiselingRecipeProvider extends ChiselingRecipeProvider {

    public CGChiselingRecipeProvider(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super("connectedglass", generator, existingFileHelper);
    }

    @Override
    protected void buildRecipes(){
        this.beginRecipe("glass")
            .add(Items.GLASS, CGGlassType.BORDERLESS_GLASS.block.asItem())
            .addConnectingItem(CGGlassType.CLEAR_GLASS.block.asItem())
            .addConnectingItem(CGGlassType.SCRATCHED_GLASS.block.asItem());

        this.beginRecipe("glass_pane")
            .add(Items.GLASS_PANE, CGGlassType.BORDERLESS_GLASS.pane.asItem())
            .addConnectingItem(CGGlassType.CLEAR_GLASS.pane.asItem())
            .addConnectingItem(CGGlassType.SCRATCHED_GLASS.pane.asItem());

        for(DyeColor color : DyeColor.values()){
            ChiselingRecipeBuilder builder = this.beginRecipe(color.getName() + "_stained_glass");

            Item glass = null;
            if(color == DyeColor.WHITE)
                glass = Items.WHITE_STAINED_GLASS;
            else if(color == DyeColor.ORANGE)
                glass = Items.ORANGE_STAINED_GLASS;
            else if(color == DyeColor.MAGENTA)
                glass = Items.MAGENTA_STAINED_GLASS;
            else if(color == DyeColor.LIGHT_BLUE)
                glass = Items.LIGHT_BLUE_STAINED_GLASS;
            else if(color == DyeColor.YELLOW)
                glass = Items.YELLOW_STAINED_GLASS;
            else if(color == DyeColor.LIME)
                glass = Items.LIME_STAINED_GLASS;
            else if(color == DyeColor.PINK)
                glass = Items.PINK_STAINED_GLASS;
            else if(color == DyeColor.GRAY)
                glass = Items.GRAY_STAINED_GLASS;
            else if(color == DyeColor.LIGHT_GRAY)
                glass = Items.LIGHT_GRAY_STAINED_GLASS;
            else if(color == DyeColor.CYAN)
                glass = Items.CYAN_STAINED_GLASS;
            else if(color == DyeColor.PURPLE)
                glass = Items.PURPLE_STAINED_GLASS;
            else if(color == DyeColor.BLUE)
                glass = Items.BLUE_STAINED_GLASS;
            else if(color == DyeColor.BROWN)
                glass = Items.BROWN_STAINED_GLASS;
            else if(color == DyeColor.GREEN)
                glass = Items.GREEN_STAINED_GLASS;
            else if(color == DyeColor.RED)
                glass = Items.RED_STAINED_GLASS;
            else if(color == DyeColor.BLACK)
                glass = Items.BLACK_STAINED_GLASS;

            builder.add(glass, CGGlassType.BORDERLESS_GLASS.getBlock(color).asItem());
            builder.addConnectingItem(CGGlassType.CLEAR_GLASS.getBlock(color).asItem());
            builder.addConnectingItem(CGGlassType.SCRATCHED_GLASS.getBlock(color).asItem());

            ChiselingRecipeBuilder paneBuilder = this.beginRecipe(color.getName() + "_stained_glass_pane");

            Item pane = null;
            if(color == DyeColor.WHITE)
                pane = Items.WHITE_STAINED_GLASS_PANE;
            else if(color == DyeColor.ORANGE)
                pane = Items.ORANGE_STAINED_GLASS_PANE;
            else if(color == DyeColor.MAGENTA)
                pane = Items.MAGENTA_STAINED_GLASS_PANE;
            else if(color == DyeColor.LIGHT_BLUE)
                pane = Items.LIGHT_BLUE_STAINED_GLASS_PANE;
            else if(color == DyeColor.YELLOW)
                pane = Items.YELLOW_STAINED_GLASS_PANE;
            else if(color == DyeColor.LIME)
                pane = Items.LIME_STAINED_GLASS_PANE;
            else if(color == DyeColor.PINK)
                pane = Items.PINK_STAINED_GLASS_PANE;
            else if(color == DyeColor.GRAY)
                pane = Items.GRAY_STAINED_GLASS_PANE;
            else if(color == DyeColor.LIGHT_GRAY)
                pane = Items.LIGHT_GRAY_STAINED_GLASS_PANE;
            else if(color == DyeColor.CYAN)
                pane = Items.CYAN_STAINED_GLASS_PANE;
            else if(color == DyeColor.PURPLE)
                pane = Items.PURPLE_STAINED_GLASS_PANE;
            else if(color == DyeColor.BLUE)
                pane = Items.BLUE_STAINED_GLASS_PANE;
            else if(color == DyeColor.BROWN)
                pane = Items.BROWN_STAINED_GLASS_PANE;
            else if(color == DyeColor.GREEN)
                pane = Items.GREEN_STAINED_GLASS_PANE;
            else if(color == DyeColor.RED)
                pane = Items.RED_STAINED_GLASS_PANE;
            else if(color == DyeColor.BLACK)
                pane = Items.BLACK_STAINED_GLASS_PANE;

            paneBuilder.add(pane, CGGlassType.BORDERLESS_GLASS.getPane(color).asItem());
            paneBuilder.addConnectingItem(CGGlassType.CLEAR_GLASS.getPane(color).asItem());
            paneBuilder.addConnectingItem(CGGlassType.SCRATCHED_GLASS.getPane(color).asItem());
        }

        ChiselingRecipeBuilder builder = this.beginRecipe("tinted_glass");
        builder.add(ConnectedGlass.tinted_glass.asItem(), CGGlassType.TINTED_BORDERLESS_GLASS.block.asItem());
        CGGlassType.TINTED_BORDERLESS_GLASS.colored_blocks.values().forEach(
            block -> builder.addConnectingItem(block.asItem())
        );
    }
}
