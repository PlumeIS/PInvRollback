package cn.plumc.invrollback;

import cn.plumc.invrollback.commands.PInvRollbackCommand;
import cn.plumc.invrollback.listeners.GameListener;
import cn.plumc.invrollback.listeners.InventoryListener;
import cn.plumc.invrollback.listeners.RollbackListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;

public final class PInvRollback extends JavaPlugin {

    public static PInvRollback instance;
    public static RollbackManager rollbackManager;

    @Override
    public void onEnable() {
        instance = this;
        rollbackManager = new RollbackManager();
        rollbackManager.load(getDataPath());
        updateConfig();

        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new RollbackListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        PInvRollbackCommand executor = new PInvRollbackCommand();
        PluginCommand pinvrollback = Bukkit.getPluginCommand("pinvrollback");
        pinvrollback.setExecutor(executor);
        pinvrollback.setTabCompleter(executor);
        getLogger().info("PInvRollback setup complete.");
    }

    @Override
    public void onDisable() {
        rollbackManager.save(getDataPath());
    }

    public void updateConfig(){
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("config.yml")));
        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
            }
        }
        saveConfig();
    }
}
