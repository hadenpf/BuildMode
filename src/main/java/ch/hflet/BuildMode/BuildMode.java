package ch.hflet.BuildMode;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BuildMode extends JavaPlugin implements Listener {
    private String pluginName;
    private String pluginVersion;
    private Logger logger;
    private PlayerRegistry registry;
    private EventListener listener;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        pluginName = getDescription().getName();
        pluginVersion = getDescription().getVersion();
        logger = getLogger();
        config = getConfig();

        registry = new PlayerRegistry(this);
        listener = new EventListener(this);

        MainCommand mainCommand = new MainCommand(this);
        getCommand("buildmode").setExecutor(mainCommand);
        getCommand("buildmode").setTabCompleter(mainCommand);

        getServer().getPluginManager().registerEvents(listener, this);

        for(Player player: registry.getRegisteredPlayers())
            registry.removePlayer(player);

        config.addDefault("deductions.flying", 1.15);
        config.addDefault("deductions.moving_while_flight_enabled", .9);
        config.addDefault("deductions.instant_breaking", 1.44);
        config.options().copyDefaults(true);
        saveConfig();

        listener.setup();

        logger.info(pluginName + " v" + pluginVersion + " has been enabled!");
    }

    @Override
    public void onDisable() {
        for(Player player: registry.getRegisteredPlayers())
            registry.removePlayer(player);

        listener.reset();

        logger.info(pluginName + " v" + pluginVersion + " has been disabled!");
    }

    public PlayerRegistry getRegistry() {
        return this.registry;
    }

    public EventListener getListener() {
        return this.listener;
    }

    void deductExp(float amount, Player player) {
        if(player.getExp() < amount && player.getLevel() == 0)
            throw new Error();

        if(player.getExp() == 0) {
            player.setLevel(player.getLevel() - 1);
            player.setExp(1 - amount);
            return;
        }

        float newExp = player.getExp() - amount;

        if(newExp < 0)
            newExp = 0;

        player.setExp(newExp);
    }

    Location getSafeLocation(Player player) {
        Location location  = player.getLocation();

        for(int y = location.getBlockY(); y >= (y - 25); y--) {
            Block block = player.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ());
            if(block.getType() != Material.AIR)
                location.setY(y + 1);
        }

        return location;
    }

    static boolean hasPermission(CommandSender sender, String permission) {
        String prefix = "buildmode";

        return (
                sender.hasPermission(prefix + ".*")
                        || sender.hasPermission(prefix + "." + permission)
                        || (
                        permission.contains(".") &&
                                sender.hasPermission(permission)
                )
        );
    }
}

/*

    MASTER TODOs

    

 */
