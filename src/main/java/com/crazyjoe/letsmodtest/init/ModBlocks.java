package com.crazyjoe.letsmodtest.init;

import com.crazyjoe.letsmodtest.block.BlockFlag;
import com.crazyjoe.letsmodtest.block.BlockLMT;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static final BlockLMT flag = new BlockFlag();

    public static void init()
    {
        GameRegistry.registerBlock(flag, "flag");
    }
}

