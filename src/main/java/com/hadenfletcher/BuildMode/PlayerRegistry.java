package com.hadenfletcher.BuildMode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;

class PlayerRegistry {
    private BuildMode plugin;
    private ArrayList<Player> registeredPlayers;

    PlayerRegistry(BuildMode plugin) {
        this.plugin = plugin;
        registeredPlayers = new ArrayList<>();
    }

    void addPlayer(Player player) {
        registeredPlayers.add(player);

        player.setAllowFlight(true);
    }

    void removePlayer(Player player) {
        registeredPlayers.remove(player);

        if(player.getGameMode() != GameMode.CREATIVE && player.isFlying()) {
            if(plugin.getSafeLocation(player) != null)
                player.teleport(plugin.getSafeLocation(player));
            player.setFlying(false);
        }

        if(player.getGameMode() != GameMode.CREATIVE)
            player.setAllowFlight(false);
    }

    boolean hasPlayer(Player player) {
        return registeredPlayers.contains(player);
    }

    ArrayList<Player> getRegisteredPlayers() {
        return registeredPlayers;
    }
}

/*

    LIST OF TODOs

    TODO: Register players by UUID instead of Player object, for cross-session enabling.

 */
