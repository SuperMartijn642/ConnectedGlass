package com.supermartijn642.connectedglass.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created 02/10/2022 by SuperMartijn642
 */
public class CGModelData {

    public static final IUnlistedProperty<CGModelData> MODEL_PROPERTY = new IUnlistedProperty<CGModelData>() {
        @Override
        public String getName(){
            return "model_data";
        }

        @Override
        public boolean isValid(CGModelData value){
            return true;
        }

        @Override
        public Class<CGModelData> getType(){
            return CGModelData.class;
        }

        @Override
        public String valueToString(CGModelData value){
            return value.toString();
        }
    };

    public static CGModelData create(IBlockAccess level, BlockPos pos, IBlockState state){
        return new CGModelData(
            new SideConnections(EnumFacing.UP, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.DOWN, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.NORTH, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.EAST, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.SOUTH, level, pos, state.getBlock()),
            new SideConnections(EnumFacing.WEST, level, pos, state.getBlock())
        );
    }

    private final Map<EnumFacing,SideConnections> sideData;
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

    public SideConnections getSideData(EnumFacing side){
        return this.sideData.get(side);
    }
}
