/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2016
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.blocks.machine.manipulator;

import buildcraft.api.statements.IActionExternal;
import mods.railcraft.api.carts.CartToolsAPI;
import mods.railcraft.common.blocks.machine.TileMachineItem;
import mods.railcraft.common.blocks.tracks.TrackTools;
import mods.railcraft.common.plugins.buildcraft.actions.Actions;
import mods.railcraft.common.plugins.buildcraft.triggers.IHasCart;
import mods.railcraft.common.plugins.buildcraft.triggers.IHasWork;
import mods.railcraft.common.plugins.forge.WorldPlugin;
import mods.railcraft.common.util.inventory.PhantomInventory;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "mods.railcraft.common.plugins.buildcraft.triggers.IHasWork", modid = "BuildCraftAPI|statements")
public abstract class TileCartManipulator extends TileMachineItem implements IHasCart, IHasWork {
    public static final float STOP_VELOCITY = 0.02f;
    public static final int PAUSE_DELAY = 4;
    private final PhantomInventory invCarts = new PhantomInventory(2, this);
    protected EntityMinecart currentCart;
    private boolean powered;
    private boolean sendCartGateAction;
    private int pause;

    @Override
    public boolean hasMinecart() {
        return currentCart != null;
    }

    public abstract boolean canHandleCart(EntityMinecart cart);

    @Override
    public boolean hasWork() {
        return currentCart != null && canHandleCart(currentCart) && (isProcessing() || !shouldSendCart(currentCart));
    }

    public abstract boolean isManualMode();

    public abstract boolean isProcessing();

    protected abstract boolean shouldSendCart(EntityMinecart cart);

    protected void sendCart(EntityMinecart cart) {
        if (cart == null)
            return;
        if (isManualMode())
            return;
        if (CartToolsAPI.cartVelocityIsLessThan(cart, STOP_VELOCITY) || cart.isPoweredCart()) {
            setPowered(true);
        }
    }

    public final boolean isPowered() {
        return powered;
    }

    protected void setPowered(boolean p) {
        if (isManualMode())
            p = false;
        if (powered != p) {
            powered = p;
            notifyBlocksOfNeighborChange();
        }
    }

    public final PhantomInventory getCartFilters() {
        return invCarts;
    }

    @Override
    public void actionActivated(IActionExternal action) {
        if (action == Actions.SEND_CART)
            sendCartGateAction = true;
        if (action == Actions.PAUSE)
            pause = PAUSE_DELAY;
    }

    public boolean isSendCartGateAction() {
        return sendCartGateAction;
    }

    public void cartWasSent() {
        sendCartGateAction = false;
    }

    public boolean isPaused() {
        return pause > 0;
    }

    @Override
    public void update() {
        super.update();
        if (Game.isClient(getWorld()))
            return;
        if (pause > 0)
            pause--;
    }

    @Override
    public final boolean canConnectRedstone(EnumFacing dir) {
        return true;
    }

    @Override
    public final boolean isPoweringTo(EnumFacing side) {
        if (!isPowered())
            return false;
        Block block = WorldPlugin.getBlock(worldObj, getPos().offset(side.getOpposite()));
        return TrackTools.isRailBlock(block) || block == Blocks.REDSTONE_WIRE || block == Blocks.POWERED_REPEATER || block == Blocks.UNPOWERED_REPEATER;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setBoolean("powered", powered);

        getCartFilters().writeToNBT("invCarts", data);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        setPowered(data.getBoolean("powered"));

        getCartFilters().readFromNBT("invCarts", data);
    }
}