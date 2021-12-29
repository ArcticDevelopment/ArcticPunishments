package dev.kyro.arcticpunishments.inventories;

import dev.kyro.arcticapi.builders.AItemStackBuilder;
import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.ASound;
import dev.kyro.arcticpunishments.enums.PunishmentReason;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class ScreensharePanel extends AGUIPanel {
	public PunishGUI punishGUI;

	public ScreensharePanel(AGUI gui) {
		super(gui);
		punishGUI = (PunishGUI) gui;
		inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 0)
				.setSlots(Material.STAINED_GLASS_PANE, 0, 11, 12, 14, 15);

		ItemStack cancel = new AItemStackBuilder(Material.ARROW)
				.setName("&c&lCANCEL")
				.setLore(new ALoreBuilder("&7Click to return to", "&7the main punish menu"))
				.getItemStack();
		getInventory().setItem(18, cancel);

		ItemStack logoutBan = new AItemStackBuilder(Material.ICE)
				.setName("&9&lLOGGING OUT")
				.setLore(new ALoreBuilder(
						"&7Player has logged out while frozen."
				))
				.getItemStack();
		getInventory().setItem(10, logoutBan);

		ItemStack admitBan = new AItemStackBuilder(Material.BEACON)
				.setName("&b&lADMITTING")
				.setLore(new ALoreBuilder(
						"&7Player admitted to cheating while frozen."
				))
				.getItemStack();
		getInventory().setItem(13, admitBan);

		ItemStack foundBan = new AItemStackBuilder(Material.ANVIL)
				.setName("&7&lBLACKLISTED MODIFICATIONS FOUND")
				.setLore(new ALoreBuilder(
						"&7Blacklisted modifications found in Screenshare."
				))
				.getItemStack();
		getInventory().setItem(16, foundBan);
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Screenshare Punish " + ChatColor.YELLOW + ((PunishGUI) gui).target.getName();
	}

	@Override
	public int getRows() {
		return 3;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getInventory().getHolder() != this) return;

		int slot = event.getSlot();
		if(slot == 18) {
			openPreviousGUI();
			return;
		} else if(slot == 10) {
			punishGUI.punish(PunishmentReason.FROZEN_LOG);
		} else if(slot == 13) {
			punishGUI.punish(PunishmentReason.SCREENSHARE_ADMITTED);
		} else if(slot == 16) {
			punishGUI.punish(PunishmentReason.SCREENSHARE);
		} else {
			return;
		}

		player.closeInventory();
		ASound.play(player, Sound.ANVIL_LAND, 1, 1);
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {

	}

	@Override
	public void onClose(InventoryCloseEvent event) {

	}
}
