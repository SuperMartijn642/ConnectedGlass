package com.supermartijn642.connectedglass;

import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.Registries;
import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;

/**
 * Created 5/15/2020 by SuperMartijn642
 */
public enum CGGlassType {

    BORDERLESS_GLASS(false, true, "Connecting"),
    CLEAR_GLASS(false, true, "Clear"),
    SCRATCHED_GLASS(false, true, "Scratched"),
    TINTED_BORDERLESS_GLASS(true, false, "Connecting Tinted");

    private final String identifier;
    public final boolean isTinted;
    public final boolean hasPanes;
    public final String translation;
    public CGGlassBlock block;
    public final List<CGGlassBlock> blocks = new ArrayList<>();
    public final EnumMap<DyeColor,CGColoredGlassBlock> colored_blocks = new EnumMap<>(DyeColor.class);
    public CGPaneBlock pane;
    public final List<CGPaneBlock> panes = new ArrayList<>();
    public final EnumMap<DyeColor,CGColoredPaneBlock> colored_panes = new EnumMap<>(DyeColor.class);

    CGGlassType(boolean isTinted, boolean hasPanes, String translation){
        this.identifier = this.name().toLowerCase(Locale.ROOT);
        this.isTinted = isTinted;
        this.hasPanes = hasPanes;
        this.translation = translation;
    }

    public String getRegistryName(){
        return this.identifier;
    }

    public String getRegistryName(DyeColor color){
        return color == null ? this.getRegistryName() : this.getRegistryName() + "_" + color.getName().toLowerCase(Locale.ROOT);
    }

    public String getPaneRegistryName(){
        return this.getRegistryName() + "_pane";
    }

    public String getPaneRegistryName(DyeColor color){
        return color == null ? this.getPaneRegistryName() : this.getRegistryName(color) + "_pane";
    }

    public void registerBlocks(RegistrationHandler.Helper<Block> helper){
        // Create uncolored block and pane
        this.block = helper.register(this.getRegistryName(),
            this.isTinted ?
                new CGTintedGlassBlock(this.name().toLowerCase(Locale.ROOT), true) :
                new CGGlassBlock(this.name().toLowerCase(Locale.ROOT), true)
        );
        this.blocks.add(this.block);
        if(this.hasPanes){
            this.pane = helper.register(this.getPaneRegistryName(), new CGPaneBlock(this.block));
            this.panes.add(this.pane);
        }

        // Create colored blocks and panes
        for(DyeColor color : DyeColor.values()){
            CGColoredGlassBlock block = helper.register(this.getRegistryName(color),
                this.isTinted ?
                    new CGColoredTintedGlassBlock(this.name().toLowerCase(Locale.ROOT) + "_" + color.name().toLowerCase(Locale.ROOT), true, color) :
                    new CGColoredGlassBlock(this.name().toLowerCase(Locale.ROOT) + "_" + color.name().toLowerCase(Locale.ROOT), true, color)
            );
            this.blocks.add(block);
            this.colored_blocks.put(color, block);
            if(this.hasPanes){
                CGColoredPaneBlock pane = helper.register(this.getPaneRegistryName(color), new CGColoredPaneBlock(block));
                this.panes.add(pane);
                this.colored_panes.put(color, pane);
            }
        }
    }

    public void registerItems(RegistrationHandler.Helper<Item> helper){
        this.blocks.forEach(block -> {
            ResourceLocation identifier = Registries.BLOCKS.getIdentifier(block);
            helper.register(identifier.getPath(), new BaseBlockItem(block, ItemProperties.create().group(ConnectedGlass.GROUP)));
        });
        this.panes.forEach(pane -> {
            ResourceLocation identifier = Registries.BLOCKS.getIdentifier(pane);
            helper.register(identifier.getPath(), new BaseBlockItem(pane, ItemProperties.create().group(ConnectedGlass.GROUP)));
        });
    }

    public CGGlassBlock getBlock(){
        return this.block;
    }

    public CGGlassBlock getBlock(DyeColor color){
        if(color == null)
            return this.block;
        return this.colored_blocks.get(color);
    }

    public CGPaneBlock getPane(){
        return this.pane;
    }

    public CGPaneBlock getPane(DyeColor color){
        if(color == null)
            return this.pane;
        return this.colored_panes.get(color);
    }
}
