package com.supermartijn642.connectedglass;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends TransparentBlock {

    public final ResourceLocation texture;
    public final boolean connected;

    public CGGlassBlock(String texture, boolean connected){
        super(Properties.of().sound(SoundType.GLASS).instrument(NoteBlockInstrument.HAT).strength(0.3f).noOcclusion().isValidSpawn((a, b, c, d) -> false).isRedstoneConductor((a, b, c) -> false).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false));
        this.texture = new ResourceLocation("connectedglass", texture);
        this.connected = connected;
    }
}
