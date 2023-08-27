package dev.kyro.arcticpunishments;

import dev.kyro.arcticapi.ArcticAPI;
import dev.kyro.arcticapi.data.AConfig;
import dev.kyro.arcticapi.data.APlayerData;
import dev.kyro.arcticpunishments.commands.MigrateCommand;
import dev.kyro.arcticpunishments.commands.PunishCommand;
import dev.kyro.arcticpunishments.commands.UnpunishCommand;
import dev.kyro.arcticpunishments.controllers.PunishManager;
import dev.kyro.arcticpunishments.controllers.PunishProfile;
import org.bukkit.plugin.java.JavaPlugin;

public class ArcticPunishments extends JavaPlugin {
	public static ArcticPunishments INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;
		ArcticAPI.configInit(this, "prefix", "error-prefix");
		APlayerData.init();
		AConfig.saveConfig();

		PunishCommand punishCommand = new PunishCommand();
		getCommand("punish").setExecutor(punishCommand);
		getCommand("p").setExecutor(punishCommand);
		UnpunishCommand unpunishCommand = new UnpunishCommand();
		getCommand("unpunish").setExecutor(unpunishCommand);
		getCommand("unp").setExecutor(unpunishCommand);

		getServer().getPluginManager().registerEvents(new PunishManager(), this);
	}

	@Override
	public void onDisable() {

	}
}
