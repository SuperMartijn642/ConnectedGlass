package com.supermartijn642.connectedglass;

import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;

/**
 * Created 5/15/2020 by SuperMartijn642
 */
public enum CGGlassType {

    BORDERLESS_GLASS(false, true), CLEAR_GLASS(false, true), SCRATCHED_GLASS(false, true), TINTED_BORDERLESS_GLASS(true, false);

    public final boolean isTinted;
    public final boolean hasPanes;
    public CGGlassBlock block;
    public final List<CGGlassBlock> blocks = new ArrayList<>();
    public final EnumMap<DyeColor,CGColoredGlassBlock> colored_blocks = new EnumMap<>(DyeColor.class);
    public CGPaneBlock pane;
    public final List<CGPaneBlock> panes = new ArrayList<>();
    public final EnumMap<DyeColor,CGColoredPaneBlock> colored_panes = new EnumMap<>(DyeColor.class);

    CGGlassType(boolean isTinted, boolean hasPanes){
        this.isTinted = isTinted;
        this.hasPanes = hasPanes;
    }

    public void init(){
        this.block = this.isTinted ?
            new CGTintedGlassBlock(this.name().toLowerCase(Locale.ROOT), true) :
            new CGGlassBlock(this.name().toLowerCase(Locale.ROOT), true);
        this.blocks.add(this.block);
        if(this.hasPanes){
            this.pane = this.block.createPane();
            this.panes.add(this.pane);
        }
        for(DyeColor color : DyeColor.values()){
            CGColoredGlassBlock block = this.isTinted ?
                new CGColoredTintedGlassBlock(this.name().toLowerCase(Locale.ROOT) + "_" + color.name().toLowerCase(Locale.ROOT), true, color) :
                new CGColoredGlassBlock(this.name().toLowerCase(Locale.ROOT) + "_" + color.name().toLowerCase(Locale.ROOT), true, color);
            this.blocks.add(block);
            this.colored_blocks.put(color, block);
            if(this.hasPanes){
                CGColoredPaneBlock pane = block.createPane();
                this.panes.add(pane);
                this.colored_panes.put(color, pane);
            }
        }
    }

    public CGGlassBlock getBlock(DyeColor color){
        if(color == null)
            return this.block;
        return this.colored_blocks.get(color);
    }

    public CGPaneBlock getPane(DyeColor color){
        if(color == null)
            return this.pane;
        return this.colored_panes.get(color);
    }

}
