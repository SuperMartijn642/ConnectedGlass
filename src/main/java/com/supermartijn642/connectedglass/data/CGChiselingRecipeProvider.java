package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.connectedglass.ConnectedGlass;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

/**
 * Created 05/04/2022 by SuperMartijn642
 */
public class CGChiselingRecipeProvider extends ChiselingRecipeProvider {

    public CGChiselingRecipeProvider(ResourceCache cache){
        super("connectedglass", cache);
    }

    @Override
    protected void buildRecipes(){
        this.beginRecipe("glass")
            .add(Item.getItemFromBlock(Blocks.GLASS), Item.getItemFromBlock(CGGlassType.BORDERLESS_GLASS.block))
            .addConnectingItem(Item.getItemFromBlock(CGGlassType.CLEAR_GLASS.block))
            .addConnectingItem(Item.getItemFromBlock(CGGlassType.SCRATCHED_GLASS.block));

        this.beginRecipe("glass_pane")
            .add(Item.getItemFromBlock(Blocks.GLASS_PANE), Item.getItemFromBlock(CGGlassType.BORDERLESS_GLASS.pane))
            .addConnectingItem(Item.getItemFromBlock(CGGlassType.CLEAR_GLASS.pane))
            .addConnectingItem(Item.getItemFromBlock(CGGlassType.SCRATCHED_GLASS.pane));

        for(EnumDyeColor color : EnumDyeColor.values()){
            ChiselingRecipeBuilder builder = this.beginRecipe(color.getName() + "_stained_glass");
            builder.add(Item.getItemFromBlock(Blocks.STAINED_GLASS), color.getMetadata(), Item.getItemFromBlock(CGGlassType.BORDERLESS_GLASS.getBlock(color)), 0);
            builder.addConnectingItem(Item.getItemFromBlock(CGGlassType.CLEAR_GLASS.getBlock(color)));
            builder.addConnectingItem(Item.getItemFromBlock(CGGlassType.SCRATCHED_GLASS.getBlock(color)));

            ChiselingRecipeBuilder paneBuilder = this.beginRecipe(color.getName() + "_stained_glass_pane");
            paneBuilder.add(Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE), color.getMetadata(), Item.getItemFromBlock(CGGlassType.BORDERLESS_GLASS.getPane(color)), 0);
            paneBuilder.addConnectingItem(Item.getItemFromBlock(CGGlassType.CLEAR_GLASS.getPane(color)));
            paneBuilder.addConnectingItem(Item.getItemFromBlock(CGGlassType.SCRATCHED_GLASS.getPane(color)));
        }

        ChiselingRecipeBuilder builder = this.beginRecipe("tinted_glass");
        builder.add(Item.getItemFromBlock(ConnectedGlass.tinted_glass), Item.getItemFromBlock(CGGlassType.TINTED_BORDERLESS_GLASS.block));
        CGGlassType.TINTED_BORDERLESS_GLASS.colored_blocks.values().forEach(
            block -> builder.addConnectingItem(Item.getItemFromBlock(block))
        );
    }
}
