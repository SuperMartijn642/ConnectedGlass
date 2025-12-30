package com.supermartijn642.connectedglass;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGColoredGlassBlock extends CGGlassBlock implements BeaconBeamBlock {

    private final DyeColor color;

    public CGColoredGlassBlock(Identifier identifier, String texture, boolean connected, DyeColor color){
        super(identifier, texture, connected);
        this.color = color;
    }

    @Override
    public DyeColor getColor(){
        return this.color;
    }
}
