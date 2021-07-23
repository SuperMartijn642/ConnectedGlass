package com.supermartijn642.connectedglass.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation,LootTable.Builder>>>,LootContextParamSet>> getTables(){
        return ImmutableList.of(Pair.of(CGBlockLootTables::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation,LootTable> lootTables, ValidationContext validationtracker){
        Set<ResourceLocation> locations = BuiltInLootTables.all().stream().filter(resourceLocation -> resourceLocation.getNamespace().equals("connectedglass")).collect(Collectors.toSet());
        for(ResourceLocation resourcelocation : Sets.difference(locations, lootTables.keySet()))
            validationtracker.reportProblem("Missing built-in table: " + resourcelocation);

        lootTables.forEach((resourceLocation, lootTable) ->
            LootTables.validate(validationtracker, resourceLocation, lootTable)
        );
    }

    @Override
    public String getName(){
        return "connectedglass:loottables";
    }
}
