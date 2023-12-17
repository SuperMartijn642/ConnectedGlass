package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.data.*;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.ResourceGenerator;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.function.Function;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod(modid = "@mod_id@", name = "@mod_name@", version = "@mod_version@", dependencies = "required-after:supermartijn642corelib@@core_library_dependency@;required-after:fusion@@fusion_dependency@")
public class ConnectedGlass {

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("connectedglass", () -> CGGlassType.BORDERLESS_GLASS.getBlock());

    @RegistryEntryAcceptor(namespace = "connectedglass", identifier = "tinted_glass", registry = RegistryEntryAcceptor.Registry.BLOCKS)
    public static CGTintedGlassBlock tinted_glass;

    public ConnectedGlass(){
        register();
        if(CommonUtils.getEnvironmentSide().isClient())
            ConnectedGlassClient.register();
        registerGenerators();
    }

    private static void register(){
        RegistrationHandler handler = RegistrationHandler.get("connectedglass");
        for(CGGlassType type : CGGlassType.values()){
            handler.registerBlockCallback(type::registerBlocks);
            handler.registerItemCallback(type::registerItems);
        }
        handler.registerBlock("tinted_glass", () -> new CGTintedGlassBlock("tinted_glass", false));
        handler.registerItem("tinted_glass", () -> new BaseBlockItem(tinted_glass, ItemProperties.create().group(GROUP)));
    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("connectedglass");
        // I have no clue why this needs to be so stupid in 1.12 to prevent the CGChiselingRecipeProvider class from loading
        //noinspection TrivialFunctionalExpressionUsage
        handler.addGenerator(cache -> ((Function<ResourceCache,ResourceGenerator>)CGChiselingRecipeProvider::new).apply(cache));
        //noinspection TrivialFunctionalExpressionUsage
        handler.addGenerator(cache -> ((Function<ResourceCache,ResourceGenerator>)CGTextureProvider::new).apply(cache));
        //noinspection TrivialFunctionalExpressionUsage
        handler.addGenerator(cache -> ((Function<ResourceCache,ResourceGenerator>)CGFusionModelGenerator::new).apply(cache));
        handler.addGenerator(CGModelGenerator::new);
        handler.addGenerator(CGBlockStateGenerator::new);
        handler.addGenerator(CGLanguageGenerator::new);
        handler.addGenerator(CGLootTableGenerator::new);
        handler.addGenerator(CGRecipeGenerator::new);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        // register to ore dict
        for(CGGlassType type : CGGlassType.values()){
            type.blocks.forEach(block -> {
                OreDictionary.registerOre("blockGlass", block);
                OreDictionary.registerOre("blockGlass", Item.getItemFromBlock(block));
            });
            if(!type.isTinted){
                OreDictionary.registerOre("blockGlassColorless", type.block);
                OreDictionary.registerOre("blockGlassColorless", Item.getItemFromBlock(type.block));
                type.colored_blocks.forEach((color, block) -> {
                    String name = color == EnumDyeColor.SILVER ? "LightGray" : color.getUnlocalizedName().toUpperCase().charAt(0) + color.getUnlocalizedName().substring(1);
                    OreDictionary.registerOre("blockGlass" + name, block);
                    OreDictionary.registerOre("blockGlass" + name, Item.getItemFromBlock(block));
                    OreDictionary.registerOre("blockGlassColored", block);
                    OreDictionary.registerOre("blockGlassColored", Item.getItemFromBlock(block));
                });
            }

            if(type.hasPanes){
                type.panes.forEach(pane -> {
                    OreDictionary.registerOre("blockPane", pane);
                    OreDictionary.registerOre("blockPane", Item.getItemFromBlock(pane));
                });

                if(!type.isTinted){
                    OreDictionary.registerOre("blockPaneColorless", type.pane);
                    OreDictionary.registerOre("blockPaneColorless", Item.getItemFromBlock(type.pane));
                    type.colored_panes.forEach((color, pane) -> {
                        String name = color == EnumDyeColor.SILVER ? "LightGray" : color.getUnlocalizedName().toUpperCase().charAt(0) + color.getUnlocalizedName().substring(1);
                        OreDictionary.registerOre("blockPane" + name, pane);
                        OreDictionary.registerOre("blockPane" + name, Item.getItemFromBlock(pane));
                        OreDictionary.registerOre("blockPaneColored", pane);
                        OreDictionary.registerOre("blockPaneColored", Item.getItemFromBlock(pane));
                    });
                }
            }
        }
    }
}
