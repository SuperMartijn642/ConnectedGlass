package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.data.*;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod("connectedglass")
public class ConnectedGlass {

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("connectedglass", () -> CGGlassType.BORDERLESS_GLASS.getBlock().asItem());

    @RegistryEntryAcceptor(namespace = "connectedglass", identifier = "tinted_glass", registry = RegistryEntryAcceptor.Registry.BLOCKS)
    public static CGTintedGlassBlock tinted_glass;

    public ConnectedGlass(){
        register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ConnectedGlassClient::register);
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
        // This needs to be a lambda in order to prevent the CGChiselingRecipeProvider class from loading
        //noinspection Convert2MethodRef
        handler.addProvider((generator, fileHelper) -> new CGChiselingRecipeProvider(generator, fileHelper));
        //noinspection Convert2MethodRef
        handler.addProvider((generator, fileHelper) -> new CGTextureProvider(generator, fileHelper));
        handler.addProvider(CGFusionModelGenerator::new);
        handler.addGenerator(CGModelGenerator::new);
        handler.addGenerator(CGBlockStateGenerator::new);
        handler.addGenerator(CGLanguageGenerator::new);
        handler.addGenerator(CGLootTableGenerator::new);
        handler.addGenerator(CGRecipeGenerator::new);
        handler.addGenerator(CGTagGenerator::new);
    }
}
