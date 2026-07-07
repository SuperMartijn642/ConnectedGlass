package com.supermartijn642.connectedglass.data;

import com.supermartijn642.connectedglass.CGGlassType;
import com.supermartijn642.rechiseled.api.ChiselingRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * Created 05/04/2022 by SuperMartijn642
 */
public class CGChiselingRecipeProvider extends ChiselingRecipeProvider {

    public CGChiselingRecipeProvider(FabricPackOutput generator){
        super("connectedglass", generator);
    }

    @Override
    protected void buildRecipes(){
        this.beginRecipe("glass")
            .entry(e -> e.regularBlock(Items.GLASS).connectingBlock(CGGlassType.BORDERLESS_GLASS.block))
            .entry(e -> e.connectingBlock(CGGlassType.CLEAR_GLASS.block.asItem()))
            .entry(e -> e.connectingBlock(CGGlassType.SCRATCHED_GLASS.block.asItem()));

        this.beginRecipe("glass_pane")
            .entry(e -> e.regularBlock(Items.GLASS_PANE).connectingBlock(CGGlassType.BORDERLESS_GLASS.pane.asItem()))
            .entry(e -> e.connectingBlock(CGGlassType.CLEAR_GLASS.pane.asItem()))
            .entry(e -> e.connectingBlock(CGGlassType.SCRATCHED_GLASS.pane.asItem()));

        for(DyeColor color : DyeColor.values()){
            ChiselingRecipeBuilder builder = this.beginRecipe(color.getName() + "_stained_glass");

            Item glass = Items.STAINED_GLASS.pick(color);
            builder.entry(e -> e.regularBlock(glass).connectingBlock(CGGlassType.BORDERLESS_GLASS.getBlock(color).asItem()));
            builder.entry(e -> e.connectingBlock(CGGlassType.CLEAR_GLASS.getBlock(color).asItem()));
            builder.entry(e -> e.connectingBlock(CGGlassType.SCRATCHED_GLASS.getBlock(color).asItem()));

            ChiselingRecipeBuilder paneBuilder = this.beginRecipe(color.getName() + "_stained_glass_pane");

            Item pane = Items.STAINED_GLASS_PANE.pick(color);
            paneBuilder.entry(e -> e.regularBlock(pane).connectingBlock(CGGlassType.BORDERLESS_GLASS.getPane(color).asItem()));
            paneBuilder.entry(e -> e.connectingBlock(CGGlassType.CLEAR_GLASS.getPane(color).asItem()));
            paneBuilder.entry(e -> e.connectingBlock(CGGlassType.SCRATCHED_GLASS.getPane(color).asItem()));
        }

        ChiselingRecipeBuilder builder = this.beginRecipe("tinted_glass");
        builder.entry(e -> e.regularBlock(Items.TINTED_GLASS).connectingBlock(CGGlassType.TINTED_BORDERLESS_GLASS.block.asItem()));
        CGGlassType.TINTED_BORDERLESS_GLASS.colored_blocks.values().forEach(
            block -> builder.entry(e -> e.connectingBlock(block.asItem()))
        );
    }
}
