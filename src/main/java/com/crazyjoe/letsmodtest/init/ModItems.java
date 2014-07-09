package com.crazyjoe.letsmodtest.init;

import com.crazyjoe.letsmodtest.item.ItemLMT;
import com.crazyjoe.letsmodtest.item.ItemMapleLeaf;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference)
public class ModItems
{
    public static final ItemLMT mapleLeaf = new ItemMapleLeaf();

    public static void init()
    {
        GameRegistry.registerItem(mapleLeaf, "mapleLeaf");
    }
}
