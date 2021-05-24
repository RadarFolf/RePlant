package de.jeff_media.replant.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReplantGUIMenuHolder implements InventoryHolder {

    private Inventory inventory;

    public void setInventory() {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
