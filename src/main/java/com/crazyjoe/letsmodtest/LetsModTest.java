package com.crazyjoe.letsmodtest;

import com.crazyjoe.letsmodtest.client.local.MulticastNetwork;
import com.crazyjoe.letsmodtest.handler.ConfigurationHandler;
import com.crazyjoe.letsmodtest.init.ModBlocks;
import com.crazyjoe.letsmodtest.init.ModItems;
import com.crazyjoe.letsmodtest.proxy.IProxy;
import com.crazyjoe.letsmodtest.reference.Reference;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class LetsModTest {

    private static MulticastNetwork network;

    @Mod.Instance(Reference.MOD_ID)
    public static LetsModTest instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

        ModItems.init();
        ModBlocks.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        network = new MulticastNetwork();
        network.startNetwork(1000);
    }
}
