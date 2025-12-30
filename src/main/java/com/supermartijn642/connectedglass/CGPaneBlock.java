package com.supermartijn642.connectedglass;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

/**
 * Created 5/11/2020 by SuperMartijn642
 */
public class CGPaneBlock extends IronBarsBlock {

    public final CGGlassBlock block;

    public CGPaneBlock(Identifier identifier, CGGlassBlock block){
        super(Properties.of().instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(0.3f).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, identifier)));
        this.block = block;
    }
}
