package com.crazyjoe.letsmodtest.creativetab;

import com.crazyjoe.letsmodtest.init.ModItems;
import com.crazyjoe.letsmodtest.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabLMT
{
    public static final CreativeTabs LMT_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem()
        {
            return ModItems.mapleLeaf;
        }

        @Override
        public String getTranslatedTabLabel()
        {
            return "Let's Mod Test";
        }
    };
}
