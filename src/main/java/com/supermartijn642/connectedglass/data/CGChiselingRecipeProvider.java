package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * Created 05/04/2022 by SuperMartijn642
 */
public class CGChiselingRecipeProvider extends ChiselingRecipeProvider {

    public CGChiselingRecipeProvider(DataGenerator generator){
        super("connectedglass", generator);
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

            Item glass = switch(color){
                case WHITE -> Items.WHITE_STAINED_GLASS;
                case ORANGE -> Items.ORANGE_STAINED_GLASS;
                case MAGENTA -> Items.MAGENTA_STAINED_GLASS;
                case LIGHT_BLUE -> Items.LIGHT_BLUE_STAINED_GLASS;
                case YELLOW -> Items.YELLOW_STAINED_GLASS;
                case LIME -> Items.LIME_STAINED_GLASS;
                case PINK -> Items.PINK_STAINED_GLASS;
                case GRAY -> Items.GRAY_STAINED_GLASS;
                case LIGHT_GRAY -> Items.LIGHT_GRAY_STAINED_GLASS;
                case CYAN -> Items.CYAN_STAINED_GLASS;
                case PURPLE -> Items.PURPLE_STAINED_GLASS;
                case BLUE -> Items.BLUE_STAINED_GLASS;
                case BROWN -> Items.BROWN_STAINED_GLASS;
                case GREEN -> Items.GREEN_STAINED_GLASS;
                case RED -> Items.RED_STAINED_GLASS;
                case BLACK -> Items.BLACK_STAINED_GLASS;
            };

            builder.add(glass, CGGlassType.BORDERLESS_GLASS.getBlock(color).asItem());
            builder.addConnectingItem(CGGlassType.CLEAR_GLASS.getBlock(color).asItem());
            builder.addConnectingItem(CGGlassType.SCRATCHED_GLASS.getBlock(color).asItem());

            ChiselingRecipeBuilder paneBuilder = this.beginRecipe(color.getName() + "_stained_glass_pane");

            Item pane = switch(color){
                case WHITE -> Items.WHITE_STAINED_GLASS_PANE;
                case ORANGE -> Items.ORANGE_STAINED_GLASS_PANE;
                case MAGENTA -> Items.MAGENTA_STAINED_GLASS_PANE;
                case LIGHT_BLUE -> Items.LIGHT_BLUE_STAINED_GLASS_PANE;
                case YELLOW -> Items.YELLOW_STAINED_GLASS_PANE;
                case LIME -> Items.LIME_STAINED_GLASS_PANE;
                case PINK -> Items.PINK_STAINED_GLASS_PANE;
                case GRAY -> Items.GRAY_STAINED_GLASS_PANE;
                case LIGHT_GRAY -> Items.LIGHT_GRAY_STAINED_GLASS_PANE;
                case CYAN -> Items.CYAN_STAINED_GLASS_PANE;
                case PURPLE -> Items.PURPLE_STAINED_GLASS_PANE;
                case BLUE -> Items.BLUE_STAINED_GLASS_PANE;
                case BROWN -> Items.BROWN_STAINED_GLASS_PANE;
                case GREEN -> Items.GREEN_STAINED_GLASS_PANE;
                case RED -> Items.RED_STAINED_GLASS_PANE;
                case BLACK -> Items.BLACK_STAINED_GLASS_PANE;
            };

            paneBuilder.add(pane, CGGlassType.BORDERLESS_GLASS.getPane(color).asItem());
            paneBuilder.addConnectingItem(CGGlassType.CLEAR_GLASS.getPane(color).asItem());
            paneBuilder.addConnectingItem(CGGlassType.SCRATCHED_GLASS.getPane(color).asItem());
        }

        ChiselingRecipeBuilder builder = this.beginRecipe("tinted_glass");
        builder.add(Items.TINTED_GLASS, CGGlassType.TINTED_BORDERLESS_GLASS.block.asItem());
        CGGlassType.TINTED_BORDERLESS_GLASS.colored_blocks.values().forEach(
            block -> builder.addConnectingItem(block.asItem())
        );
    }
}
