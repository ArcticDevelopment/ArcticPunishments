package dev.kyro.arcticpunishments.inventories;

import dev.kyro.arcticapi.builders.AItemStackBuilder;
import dev.kyro.arcticapi.builders.ALoreBuilder;
import dev.kyro.arcticapi.data.APlayer;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticapi.gui.AGUI;
import dev.kyro.arcticapi.gui.AGUIPanel;
import dev.kyro.arcticapi.misc.AOutput;
import dev.kyro.arcticapi.misc.ASound;
import dev.kyro.arcticpunishments.enums.PermissionLevel;
import dev.kyro.arcticpunishments.enums.PunishmentReason;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class PunishPanel extends AGUIPanel {
	public PunishGUI punishGUI;

	public PunishPanel(AGUI gui) {
		super(gui);
		punishGUI = (PunishGUI) gui;

		APlayer aPlayer = APlayerData.getPlayerData(punishGUI.target);
		FileConfiguration playerData = aPlayer.playerData;

		inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 0)
				.setSlots(Material.STAINED_GLASS_PANE, 0, 11, 19, 20, 21, 22, 23, 24, 25, 29);

		ItemStack muteInfo = new AItemStackBuilder(Material.BOOK)
				.setName("&6&lMUTE INFORMATION")
				.setLore(new ALoreBuilder(
						"&e" + punishGUI.target.getName() + " &7has &6" + playerData.getInt("mutes") + " &7mutes",
						"&7Malice: " + (playerData.getBoolean("malice") ? "&eYes" : "&eNo")
				))
				.getItemStack();
		getInventory().setItem(10, muteInfo);

		ItemStack spamMute = new AItemStackBuilder(Material.SUGAR)
				.setName("&f&lSPAMMING")
				.setLore(new ALoreBuilder(
						"&7Player is excessively sending messages in chat.", "", "&7- &eSending messages too quickly", "&7- &eAdvertising"
				))
				.getItemStack();
		getInventory().setItem(12, spamMute);

		ItemStack disrespectMute = new AItemStackBuilder(Material.RAW_FISH, 1, 3)
				.setName("&e&lRACISM/DISRESPECT")
				.setLore(new ALoreBuilder(
						"&7Player is discriminating another player.", "", "&7- &eExcessive toxicity", "&7- &eRudely saying something of another player.", "&7- &eRacist/Homophobic behavior"
				))
				.getItemStack();
		getInventory().setItem(13, disrespectMute);

		ItemStack maliciousMute = new AItemStackBuilder(Material.TNT)
				.setName("&c&lMALICIOUS ACTIONS")
				.setLore(new ALoreBuilder(
						"&7Player is attempting to harm other players.", "", "&7- &eAttempting to scam players", "&7- &eSpreading malicious software", "&7- &eDDoS threat/Leaking private information"
				))
				.getItemStack();
		getInventory().setItem(14, maliciousMute);

		ItemStack evadingMute = new AItemStackBuilder(Material.SKULL_ITEM, 1, 0)
				.setName("&7&lEVADING MUTE")
				.setLore(new ALoreBuilder(
						"&7Player is bypassing a previous mute.", "", "&7- &eTalking on another account while muted"
				))
				.getItemStack();
		getInventory().setItem(16, evadingMute);





		ItemStack banInfo = new AItemStackBuilder(Material.BOOK)
				.setName("&6&lBAN INFORMATION")
				.setLore(new ALoreBuilder(
						"&e" + punishGUI.target.getName() + " &7has &6" + playerData.getInt("bans") + " &7bans"
				))
				.getItemStack();
		getInventory().setItem(28, banInfo);

		ItemStack cheatingBan = new AItemStackBuilder(Material.DIAMOND_SWORD)
				.setName("&b&lCHEATING")
				.setLore(new ALoreBuilder(
						"&7Playing is obtaining an advantage by cheating.", "", "&7- &ePlayer is using blacklisted modifications", "&7- &ePlayer has flagged the Anti-Cheat"
				))
				.getItemStack();
		getInventory().setItem(30, cheatingBan);

		ItemStack screenshareBan = new AItemStackBuilder(Material.EYE_OF_ENDER)
				.setName("&a&lSCREENSHARE")
				.setLore(new ALoreBuilder(
						"&7Click to view Screenshare options."
				))
				.getItemStack();
		getInventory().setItem(31, screenshareBan);

		ItemStack boostingBan = new AItemStackBuilder(Material.INK_SACK, 1, 15)
				.setName("&f&lBOOSTING")
				.setLore(new ALoreBuilder(
						"&7Player is improving stats through boosting.", "", "&7- &ePlayer is feeding kills to another player", "&7- &ePlayer is being feed kills by another player"
				))
				.getItemStack();
		getInventory().setItem(32, boostingBan);

		ItemStack compromiseBan = new AItemStackBuilder(Material.MONSTER_EGG, 1, 50)
				.setName("&7&lCOMPROMISED ACCOUNT")
				.setLore(new ALoreBuilder(
						"&7Player is using a compromised account.", "", "&7- &eAccount is clearly an NFA, or SFA", "&7- &ePlayer's account is being used by another person"
				))
				.getItemStack();
		getInventory().setItem(33, compromiseBan);

		ItemStack evadingBan = new AItemStackBuilder(Material.SKULL_ITEM, 1, 1)
				.setName("&8&lEVADING BAN")
				.setLore(new ALoreBuilder(
						"&7Player is evading a previous ban.", "", "&7- &ePlayer has banned accounts on their IP", "&7- &ePlayer is using an alt while banned"
				))
				.getItemStack();
		getInventory().setItem(34, evadingBan);
	}

	@Override
	public String getName() {
		return ChatColor.GRAY + "Punish " + ChatColor.YELLOW + ((PunishGUI) gui).target.getName();
	}

	@Override
	public int getRows() {
		return 5;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getInventory().getHolder() != this) return;

		PermissionLevel permissionLevel = PermissionLevel.getPermissionLevel(player);
		int slot = event.getSlot();
		if(slot == 12) {
			punishGUI.punish(PunishmentReason.SPAMMING);
		} else if(slot == 13) {
			punishGUI.punish(PunishmentReason.RACISM_DISRESPECT);
		} else if(slot == 14) {
			punishGUI.punish(PunishmentReason.MALICIOUS_ACTIONS);
		} else if(slot == 16) {
			punishGUI.punish(PunishmentReason.MUTE_EVADING);
		} else if(slot == 30) {
			punishGUI.punish(PunishmentReason.CHEATING);
		} else if(slot == 31) {
			if(permissionLevel != PermissionLevel.SCREENSHARE) {
				AOutput.error(player, "You do not have permissions to ban players for screenshare reasons");
				return;
			}

			openPanel(punishGUI.screensharePanel);
			return;
		} else if(slot == 32) {
			punishGUI.punish(PunishmentReason.BOOSTING);
		} else if(slot == 33) {
			punishGUI.punish(PunishmentReason.COMPROMISED);
		} else if(slot == 34) {
			punishGUI.punish(PunishmentReason.BAN_EVADING);
		} else {
			return;
		}

		player.closeInventory();
		ASound.play(player, Sound.ANVIL_LAND, 1, 1);
	}

	@Override
	public void onOpen(InventoryOpenEvent event) { }

	@Override
	public void onClose(InventoryCloseEvent event) { }
}
