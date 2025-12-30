package com.supermartijn642.connectedglass;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
public class CGGlassBlock extends TransparentBlock {

    public final Identifier texture;
    public final boolean connected;

    public CGGlassBlock(Identifier identifier, String texture, boolean connected){
        super(Properties.of().sound(SoundType.GLASS).instrument(NoteBlockInstrument.HAT).strength(0.3f).noOcclusion().isValidSpawn((a, b, c, d) -> false).isRedstoneConductor((a, b, c) -> false).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false).setId(ResourceKey.create(Registries.BLOCK, identifier)));
        this.texture = Identifier.fromNamespaceAndPath("connectedglass", texture);
        this.connected = connected;
    }
}
