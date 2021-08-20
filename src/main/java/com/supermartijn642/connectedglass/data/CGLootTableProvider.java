package com.supermartijn642.connectedglass.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created 6/23/2020 by SuperMartijn642
 */
public class CGLootTableProvider extends LootTableProvider {

    public CGLootTableProvider(DataGenerator dataGeneratorIn){
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation,LootTable.Builder>>>,LootParameterSet>> getTables(){
        return ImmutableList.of(Pair.of(CGBlockLootTables::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation,LootTable> lootTables, ValidationTracker validationtracker){
        Set<ResourceLocation> locations = LootTables.all().stream().filter(resourceLocation -> resourceLocation.getNamespace().equals("connectedglass")).collect(Collectors.toSet());
        for(ResourceLocation resourcelocation : Sets.difference(locations, lootTables.keySet()))
            validationtracker.reportProblem("Missing built-in table: " + resourcelocation);

        lootTables.forEach((resourceLocation, lootTable) ->
            LootTableManager.validate(validationtracker, resourceLocation, lootTable)
        );
    }

    @Override
    public String getName(){
        return "connectedglass:loottables";
    }
}
