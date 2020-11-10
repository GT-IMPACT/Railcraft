/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.plugins.forestry;

import cpw.mods.fml.common.Optional;
import forestry.api.storage.IBackpackDefinition;
import mods.railcraft.common.plugins.thaumcraft.ResearchItemRC;
import mods.railcraft.common.plugins.thaumcraft.ThaumcraftPlugin;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


/**
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
@Optional.Interface(iface = "forestry.api.storage.IBackpackDefinition", modid = "Forestry")
public class ApothecariesBackpack extends BaseBackpack implements IBackpackDefinition {

    private static ApothecariesBackpack instance;

    public static ApothecariesBackpack getInstance() {
        if (instance == null)
            instance = new ApothecariesBackpack();
        return instance;
    }

    protected ApothecariesBackpack() {
    }

    public void setup() {
        addItem(Items.potionitem);
        addItem(Items.glass_bottle);
    }

    @Override
    public String getKey() {
        return "APOTHECARY";
    }

    @Override
    public int getPrimaryColour() {
        return 16262179;
    }

    @Override
    public int getSecondaryColour() {
        return 0xFFFFFF;
    }


}
