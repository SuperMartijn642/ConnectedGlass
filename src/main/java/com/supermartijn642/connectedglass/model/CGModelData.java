package com.supermartijn642.connectedglass.model;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created 02/10/2022 by SuperMartijn642
 */
public class CGModelData {

    public static final ModelProperty<CGModelData> MODEL_PROPERTY = new ModelProperty<>();

    public static CGModelData create(BlockGetter level, BlockPos pos, BlockState state){
        return new CGModelData(
            new SideConnections(Direction.UP, level, pos, state.getBlock()),
            new SideConnections(Direction.DOWN, level, pos, state.getBlock()),
            new SideConnections(Direction.NORTH, level, pos, state.getBlock()),
            new SideConnections(Direction.EAST, level, pos, state.getBlock()),
            new SideConnections(Direction.SOUTH, level, pos, state.getBlock()),
            new SideConnections(Direction.WEST, level, pos, state.getBlock())
        );
    }

    private final Map<Direction,SideConnections> sideData;
    protected int hashCode;

    protected CGModelData(SideConnections up, SideConnections down, SideConnections north, SideConnections east, SideConnections south, SideConnections west){
        //noinspection unchecked
        this.sideData = Map.ofEntries(Stream.of(up, down, north, east, south, west).filter(Objects::nonNull).map(connections -> Map.entry(connections.side, connections)).toArray(Map.Entry[]::new));
        // The side is included in the hashCode for SidesModelData, so we can simply add up all the hash codes
        this.hashCode = this.sideData.hashCode();
    }

    @Override
    public int hashCode(){
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof CGModelData && this.hashCode == ((CGModelData)obj).hashCode;
    }

    public SideConnections getSideData(Direction side){
        return this.sideData.get(side);
    }
}
