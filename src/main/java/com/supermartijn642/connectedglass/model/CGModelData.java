package com.supermartijn642.connectedglass.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 02/10/2022 by SuperMartijn642
 */
public class CGModelData {

    public static final ModelProperty<CGModelData> MODEL_PROPERTY = new ModelProperty<>();

    public static CGModelData create(IBlockReader level, BlockPos pos, BlockState state){
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
        this.sideData = ImmutableMap.copyOf(Stream.of(up, down, north, east, south, west).filter(Objects::nonNull).collect(Collectors.toMap(a -> a.side, connections -> connections)));
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
