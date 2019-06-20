package ch.hflet.BuildMode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private BuildMode plugin;
    private PlayerRegistry registry;

    MainCommand(BuildMode plugin){
        this.plugin = plugin;
        registry = plugin.getRegistry();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;
        Player player = null;

        if(isPlayer)
            player = (Player) sender;

        if(args.length == 0) {
            if(!isPlayer)
                sendError(sender, "You must be a player to use this!");
            else if(!hasPermission(sender, "use"))
                sendError(sender, "You don't have permission to use this!");
            else {
                if(!registry.hasPlayer(player)) {
                    registry.addPlayer(player);
                    sender.sendMessage(ChatColor.GREEN + "Build Mode has been enabled!");
                } else {
                    registry.removePlayer(player);
                    sender.sendMessage(ChatColor.GREEN + "Build Mode has been disabled!");
                }
            }
        } else {
            switch(args[0]) {
                case "enable":
                    if(!isPlayer)
                        sendError(sender, "You must be a player to use this!");
                    else if(!hasPermission(sender, "use"))
                        sendError(sender, "You don't have permission to use this!");
                    else {
                        registry.addPlayer(player);
                        sender.sendMessage(ChatColor.GREEN + "Build Mode has been enabled!");
                    }
                    break;
                case "disable":
                    if(!isPlayer)
                        sendError(sender, "You must be a player to use this!");
                    else if(!hasPermission(sender, "use"))
                        sendError(sender, "You don't have permission to use this!");
                    else {
                        if(registry.hasPlayer(player)) {
                            registry.removePlayer(player);
                            sender.sendMessage(ChatColor.GREEN + "Build Mode has been disabled!");
                        } else {
                            sendError(sender, "You used an invalid argument!", cmd, true);
                        }
                    }
                    break;
                default:
                    sendError(sender, "You used an invalid argument!", cmd, true);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> list = new ArrayList<String>();

        if(args.length == 1) {
            list.add("enable");
            list.add("disable");
        }

        return list;
    }

    private String errorLine(String prefix, String message) {
        return ChatColor.DARK_RED + prefix + ChatColor.RED + message;
    }

    private String errorLine(String message) {
        return errorLine("Error: ", message);
    }

    private void sendError(CommandSender sender, String message, Command cmd, boolean showUsage) {
        String output = "";

        output += errorLine("Error: ", message);
        if(showUsage) output += "\n" + errorLine("Usage: ", cmd.getUsage());

        sender.sendMessage(output);
    }

    private void sendError(CommandSender sender, String message, Command cmd) {
        sendError(sender, message, cmd, true);
    }

    private void sendError(CommandSender sender, String message) {
        sender.sendMessage(errorLine(message));
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        return BuildMode.hasPermission(sender, permission);
    }
}
