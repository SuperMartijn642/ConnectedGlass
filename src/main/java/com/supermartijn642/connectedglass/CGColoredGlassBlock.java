package com.supermartijn642.connectedglass;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredGlassBlock extends CGGlassBlock implements BeaconBeamBlock {

    private DyeColor color;

    public CGColoredGlassBlock(String registryName, String texture, boolean connected, DyeColor color){
        super(registryName, texture, connected);
        this.color = color;
    }

    public CGColoredGlassBlock(String registryName, boolean connected, DyeColor color){
        this(registryName, registryName, connected, color);
    }

    @Override
    public DyeColor getColor(){
        return this.color;
    }

    @Override
    public CGColoredPaneBlock createPane(){
        return new CGColoredPaneBlock(this);
    }
}
