package com.supermartijn642.connectedglass;

import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.item.DyeColor;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredGlassBlock extends CGGlassBlock implements IBeaconBeamColorProvider {

    private final DyeColor color;

    public CGColoredGlassBlock(String texture, boolean connected, DyeColor color){
        super(texture, connected);
        this.color = color;
    }

    @Override
    public DyeColor getColor(){
        return this.color;
    }
}
